CREATE TABLE IF NOT EXISTS asn (
    network CIDR PRIMARY KEY,
    asn INTEGER NOT NULL,
    organization TEXT NOT NULL
);