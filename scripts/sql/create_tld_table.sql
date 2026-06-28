CREATE TABLE IF NOT EXISTS tld (
    tld TEXT PRIMARY KEY,
    type TEXT,
    registry TEXT,
    country_code CHAR(2),
    iana_id TEXT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    status TEXT,
    whois_server_url TEXT,
    rdap_server_url TEXT
);