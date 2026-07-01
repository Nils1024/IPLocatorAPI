# IPLocatorAPI
IP Locator is an API to get information about IP-Addresses, Domains, TLDs or ASNs.

> [!NOTE]
> Only IPv4 is supported and my DNS Server cannot find every domain atm

## Demo
You can try it on https://ipapi.nilsb.tech/

### Endpoints
- /v1/ip/{ip}
- /v1/domain/{domain}
- /v1/tld/{tld}
- /v1/asn/{asn}

## Other projects using this API
I also created a slackbot and a website that use this API:

- https://github.com/Nils1024/IPLocatorWebsite
- https://github.com/Nils1024/IPLocatorSlackBot

## How to deploy it by yourself?
1. Setup a PostgreSQL database
2. Create the following environment variables:
   1. IPLocatorAPI_DB_URL - Your PostgreSQL database URL (e.g. localhost)
   2. IPLocatorAPI_DB_NAME - Your PostgreSQL database name
   3. IPLocatorAPI_DB_USER - Your database user
   4. IPLocatorAPI_DB_PASSWD - Your database password
3. Download the GeoLite2 City and ASN CSV Files (I cant provide them due to copyright)
4. Drop the following CSV files in the /scripts/csv/ folder:
   1. GeoLite2-ASN-Blocks-IPv4.csv 
   2. GeoLite2-ASN-Blocks-IPv6.csv 
   3. GeoLite2-City-Blocks-IPv4.csv 
   4. GeoLite2-City-Blocks-IPv6.csv 
   5. GeoLite2-City-Locations-en.csv
5. run newDB.py (This will run for a few minutes)
6. run ./gradlew bootJar
7. Move the .jar file from build/libs/ to your server
8. Make sure Java is installed
9. Run the jar

## AI declaration
I used AI to get started on how to get information on ip-addresses. I also used the JetBrains auto completion but at some point these suggestions were horrible. On this project I also used Codex with a local run Qwen3.6 35b model but he made so many unnecessary changes that I ended up cleaning his mess up longer than I probably needed to code this by myself.