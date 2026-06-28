import io
import sys
import threading
import csv
import psycopg2
import os
import json
from urllib.request import urlopen, Request

def connect(host, database, user, password):
    return psycopg2.connect(
        host=host,
        database=database,
        user=user,
        password=password,
        port=5432
    )

def reset_schema(db_conn):
    with db_conn.cursor() as cur:
        cur.execute("DROP SCHEMA public CASCADE;")
        cur.execute("CREATE SCHEMA public;")

    db_conn.commit()

def create_schema(db_conn):
    scripts = [
        "./sql/create_networks_table.sql",
        "./sql/create_geolocations_table.sql",
        "./sql/create_asn_table.sql",
        "./sql/create_rdap_networks_table.sql",
        "./sql/create_rdap_domains_table.sql",
        "./sql/create_tld_table.sql"
    ]

    with db_conn.cursor() as cur:
        for script in scripts:
            with open(script, "r", encoding="utf-8") as f:
                sql = f.read()
                cur.execute(sql)

    db_conn.commit()

def copy_csv_to_table(cur, path, table, db_columns, csv_columns):
    buffer = io.StringIO()
    writer = csv.writer(buffer)

    with open(path, "r", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            writer.writerow([row[col] if row[col] else None for col in csv_columns])

    buffer.seek(0)
    cur.copy_expert(
        "COPY %s (%s) FROM STDIN WITH (FORMAT CSV, NULL '')" % (table, ", ".join(db_columns)),
        buffer
    )

def import_asn(db_conn):
    csv_cols = ("network", "autonomous_system_number", "autonomous_system_organization")
    db_cols = ("network", "asn", "organization")

    files = [
        "./csv/GeoLite2-ASN-Blocks-IPv4.csv",
        "./csv/GeoLite2-ASN-Blocks-IPv6.csv"
    ]

    with db_conn.cursor() as cur:
        for path in files:
            copy_csv_to_table(cur, path, "asn", db_cols, csv_cols)
    db_conn.commit()

def import_geolocation(db_conn):
    csv_cols = ("geoname_id", "continent_code", "country_iso_code", "subdivision_1_name", "city_name", "time_zone")
    db_cols = ("geoname_id", "continent_code", "country_code", "region", "city_name", "time_zone")

    path = "./csv/GeoLite2-City-Locations-en.csv"

    with db_conn.cursor() as cur:
        copy_csv_to_table(cur, path, "geolocations", db_cols, csv_cols)
    db_conn.commit()

def import_networks(db_conn):
    cols = ("network", "geoname_id", "postal_code", "latitude", "longitude", "accuracy_radius")

    files = [
        "./csv/GeoLite2-City-Blocks-IPv4.csv",
        "./csv/GeoLite2-City-Blocks-IPv6.csv"
    ]

    with db_conn.cursor() as cur:
        for path in files:
            copy_csv_to_table(cur, path, "networks", cols, cols)
    db_conn.commit()

def import_geolite2(host, database, user, password):
    errors = []

    def run(fn):
        conn = connect(host, database, user, password)

        try:
            fn(conn)
            print("Imported <%s> successfully" % fn.__name__)
        except Exception as e:
            errors.append((fn.__name__, e))
        finally:
            conn.close()

    threads = [
        threading.Thread(target=run, args=(import_asn,)),
        threading.Thread(target=run, args=(import_networks,)),
        threading.Thread(target=run, args=(import_geolocation,))
    ]

    for t in threads:
        t.start()
    for t in threads:
        t.join()

    if errors:
        for name, error in errors:
            print("Error in <%s>: %s" % (name, error), file=sys.stderr)
        raise RuntimeError("One or more imports failed")

def import_tld_data(host, database, user, password):
    conn = connect(host, database, user, password)

    try:
        tlds = _fetch_iana_tld_list()
        print(f"Fetched {len(tlds)} TLDs from IANA list")

        with conn.cursor() as cur:
            for tld in sorted(tlds):
                cur.execute(
                    "INSERT INTO tld (tld) VALUES (%s) ON CONFLICT (tld) DO NOTHING",
                    (tld,)
                )
        conn.commit()
        print(f"Inserted {len(tlds)} TLDs")

        # 2. RDAP- und WHOIS-Server-URLs ergänzen
        rdap_map = _fetch_iana_rdap_bootstrap()
        print(f"Fetched RDAP data for {len(rdap_map)} TLDs")

        count = 0
        with conn.cursor() as cur:
            for tld, rdap_url in rdap_map.items():
                whois_url = rdap_url.replace("https://", "whois://").rstrip("/")
                cur.execute(
                    """UPDATE tld SET
                        rdap_server_url  = %s,
                        whois_server_url = %s
                     WHERE tld = %s""",
                    (rdap_url, whois_url, tld)
                )
                if cur.rowcount > 0:
                    count += 1

        conn.commit()
        print(f"Updated {count} TLDs with RDAP server URLs")

    except Exception as e:
        conn.rollback()
        print(f"TLD import failed: {e}", file=sys.stderr)
        raise
    finally:
        conn.close()

def _fetch_iana_rdap_bootstrap() -> dict:
    url = "https://data.iana.org/rdap/dns.json"
    req = Request(url, headers={"User-Agent": "Mozilla/5.0"})

    with urlopen(req, timeout=30) as resp:
        data = json.loads(resp.read().decode("utf-8"))

    result = {}
    for service in data.get("services", []):
        if len(service) != 2:
            continue
        tld_entries, server_list = service
        if not server_list:
            continue
        rdap_url = server_list[0].rstrip("/")
        for tld in tld_entries:
            normalized = ("." + tld.lower()) if not tld.startswith(".") else tld.lower()
            result[normalized] = rdap_url

    return result

def _fetch_iana_tld_list() -> set:
    url = "https://data.iana.org/TLD/tlds-alpha-by-domain.txt"
    req = Request(url, headers={"User-Agent": "Mozilla/5.0"})

    with urlopen(req, timeout=30) as resp:
        lines = resp.read().decode("utf-8").splitlines()

    return {
        "." + line.strip().lower()
        for line in lines
        if line.strip() and not line.startswith("#")
    }

def create_indices(host, database, user, password):
    conn = connect(host, database, user, password)

    with conn.cursor() as cur:
        with open("./sql/create_indices.sql", "r", encoding="utf-8") as f:
            sql = f.read()
            cur.execute(sql)

    conn.commit()

def main():
    host = os.getenv("IPLocatorAPI_DB_URL")
    database = os.getenv("IPLocatorAPI_DB_NAME")
    user = os.getenv("IPLocatorAPI_DB_USER")
    password = os.getenv("IPLocatorAPI_DB_PASSWD")

    if not all([host, database, user, password]):
        print("Error: All DB env vars required (DB_URL, DB_NAME, DB_USER, DB_PASSWD)")
        sys.exit(1)

    print("Connecting to <%s>/<%s> with user <%s>" % (host, database, user))
    conn = connect(host, database, user, password)
    print("Connected to <%s>/<%s> with user <%s>" % (host, database, user))

    reset_schema(conn)
    print("Schema reset successful")

    create_schema(conn)
    print("Schema created successfully")
    conn.close()

    import_geolite2(host, database, user, password)
    print("Geolite2 imported successfully")

    import_tld_data(host, database, user, password)
    print("TLDs import successfully")

    create_indices(host, database, user, password)
    print("Indices created successfully")

if __name__ == "__main__":
    main()
    exit(0)