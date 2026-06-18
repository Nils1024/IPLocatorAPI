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
    with db_conn.cursor() as cur:
        scripts = [
            "./sql/create_networks_table.sql",
            "./sql/create_geolocation_table.sql",
            "./sql/create_rdap_networks_table.sql",
            "./sql/create_rdap_domains_table.sql",
            "./sql/create_tld_table.sql"
        ]

        for script in scripts:
            with open(script, "r", encoding="utf-8") as f:
                sql = f.read()
                cur.execute(sql)

    db_conn.commit()

def import_networks(db_conn):
    with open("./sql/insert_into_network.sql", "r", encoding="utf-8") as sql_file:
        sql = sql_file.read()

        with db_conn.cursor() as cur:
            with open("./csv/GeoLite2-ASN-Blocks-IPv4.csv", "r", encoding="utf-8") as f:
                reader = csv.DictReader(f)

                rows = [
                    (row["network"],
                     row["autonomous_system_number"],
                     row["autonomous_system_organization"])
                    for row in reader
                ]

                execute_values(cur, sql, rows, page_size=5000)

            with open("./csv/GeoLite2-ASN-Blocks-IPv6.csv", "r", encoding="utf-8") as f:
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

def import_geolite2(db_conn):
    import_networks(db_conn)
    print("Networks imported successfully")

    import_geolocation(db_conn)
    print("Geolocation imported successfully")

    db_conn.commit()

host = os.getenv("IPLocatorAPI_DB_URL")
database = os.getenv("IPLocatorAPI_DB_NAME")
user = os.getenv("IPLocatorAPI_DB_USER")
print("Connecting to <%s>/<%s> with user <%s>" % (host, database, user))
conn = connect(host,
               database,
               user,
               os.getenv("IPLocatorAPI_DB_PASSWD"))
print("Connected to <%s>/<%s> with user <%s>" % (host, database, user))

reset_schema(conn)
print("Schema reset successful")

create_schema(conn)
print("Schema created successfully")

import_geolite2(conn)

conn.close()
