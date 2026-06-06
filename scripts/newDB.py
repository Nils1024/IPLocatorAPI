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

host = os.getenv("IPLocatorAPI_DB_URL")
user = os.getenv("IPLocatorAPI_DB_USER")
print("Connecting to <%s> with user <%s>" % (host, user))
conn = connect(host,
               "iplocatorapi_db",
               user,
               os.getenv("IPLocatorAPI_DB_PASSWD"))
print("Connected to <%s> with user <%s>" % (host, user))
reset_schema(conn)
print("Schema reset successful")
conn.close()
