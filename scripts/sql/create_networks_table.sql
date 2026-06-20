CREATE TABLE IF NOT EXISTS networks (
    network CIDR PRIMARY KEY,
    geoname_id INTEGER,
    postal_code TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    accuracy_radius INTEGER
);