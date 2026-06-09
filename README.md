# IPLocatorAPI

## How to deploy?
1. Setup a PostgreSQL database
2. Create following environment variables:
   1. IPLocatorAPI_DB_URL - Your PostgreSQL database URL (e.g. localhost)
   2. IPLocatorAPI_DB_NAME - Your PostgreSQL database name
   3. IPLocatorAPI_DB_USER - Your database user
   4. IPLocatorAPI_DB_PASSWD - Your database password
3. Download the GeoLite2 City and ASN CSV Files
4. Drop following CSV files in the /scripts/csv/ folder:
   1. GeoLite2-ASN-Blocks-IPv4.csv 
   2. GeoLite2-ASN-Blocks-IPv6.csv 
   3. GeoLite2-City-Blocks-IPv4.csv 
   4. GeoLite2-City-Blocks-IPv6.csv 
   5. GeoLite2-City-Locations-en.csv
5. run newDB.py (This will run for a few minutes)
6. Start the API