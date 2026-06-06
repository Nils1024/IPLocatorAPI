CREATE TABLE IF NOT EXISTS rdap_domains (
    domain TEXT PRIMARY KEY,
    registrar TEXT,
    registration_date TIMESTAMP,
    expiration_date TIMESTAMP,
    registrant_country_code CHAR(2),
    nameservers TEXT[],
    status TEXT[],
    last_refresh TIMESTAMP NOT NULL
);