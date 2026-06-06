CREATE TABLE IF NOT EXISTS rdap_networks (
    network CIDR PRIMARY KEY,
    organization TEXT,
    handle TEXT,
    registry TEXT,
    abuse_email TEXT,
    abuse_phone TEXT,
    country_code CHAR(2),
    start_address INET,
    end_address INET,
    registration_date TIMESTAMP,
    last_refresh TIMESTAMP NOT NULL
);