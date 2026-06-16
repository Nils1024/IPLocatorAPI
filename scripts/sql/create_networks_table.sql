CREATE TABLE IF NOT EXISTS networks (
    network CIDR PRIMARY KEY,
    geoname_id INTEGER,
    asn INTEGER NOT NULL,
    organization TEXT NOT NULL,
    postal_code TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    accuracy_radius INTEGER
);