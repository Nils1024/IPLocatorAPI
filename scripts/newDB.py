import sys
import threading

from psycopg2.extras import execute_values

import csv
import psycopg2
import os

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

def import_asn(db_conn):
    with open("./sql/insert_into_asn.sql", "r", encoding="utf-8") as sql_file:
        sql = sql_file.read()

        files = [
            "./csv/GeoLite2-ASN-Blocks-IPv4.csv",
            "./csv/GeoLite2-ASN-Blocks-IPv6.csv"
        ]

        with db_conn.cursor() as cur:
            for path in files:
                with open(path, "r", encoding="utf-8") as f:
                    reader = csv.DictReader(f)

                    rows = [
                        (row["network"],
                         row["autonomous_system_number"],
                         row["autonomous_system_organization"])
                        for row in reader
                    ]

                    execute_values(cur, sql, rows, page_size=5000)

def import_geolocation(db_conn):
    with open("./sql/insert_into_geolocation.sql", "r", encoding="utf-8") as sql_file:
        sql = sql_file.read()

        with db_conn.cursor() as cur:
            with open("./csv/GeoLite2-City-Locations-en.csv", "r", encoding="utf-8") as f:
                reader = csv.DictReader(f)
                rows = []
                for row in reader:
                    rows.append((
                        int(row["geoname_id"]),
                        row.get("continent_code") or None,
                        row.get("country_iso_code") or None,
                        row.get("subdivision_1_name") or None,
                        row.get("city_name") or None,
                        row.get("timezone") or None
                    ))

                execute_values(cur, sql, rows, page_size=5000)

def import_networks(db_conn):
    with open("./sql/insert_into_networks.sql", "r", encoding="utf-8") as sql_file:
        sql = sql_file.read()

        files = [
            "./csv/GeoLite2-City-Blocks-IPv4.csv",
            "./csv/GeoLite2-City-Blocks-IPv6.csv"
        ]

        with db_conn.cursor() as cur:
            for path in files:
                with open(path, "r", encoding="utf-8") as f:
                    reader = csv.DictReader(f)

                    rows = [
                        (row["network"],
                         int(row["geoname_id"]) if row["geoname_id"] else None,
                         row["postal_code"] or None,
                         float(row["latitude"]) if row["latitude"] else None,
                         float(row["longitude"]) if row["longitude"] else None,
                         int(row["accuracy_radius"]) if row["accuracy_radius"] else None)
                        for row in reader
                    ]

                    execute_values(cur, sql, rows, page_size=5000)

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

def main():
    host = os.getenv("IPLocatorAPI_DB_URL")
    database = os.getenv("IPLocatorAPI_DB_NAME")
    user = os.getenv("IPLocatorAPI_DB_USER")
    password = os.getenv("IPLocatorAPI_DB_PASSWD")

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

if __name__ == "__main__":
    main()
    exit(0)