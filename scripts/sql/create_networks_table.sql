CREATE TABLE IF NOT EXISTS networks (
    network CIDR PRIMARY KEY,
    asn INTEGER NOT NULL,
    organization TEXT NOT NULL
);