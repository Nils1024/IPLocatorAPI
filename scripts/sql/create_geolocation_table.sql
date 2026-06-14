CREATE TABLE IF NOT EXISTS geolocation (
    network CIDR PRIMARY KEY,
    continent_code CHAR(2),
    country_code CHAR(2),
    postal_code TEXT,
    region TEXT,
    city_name TEXT,
    accuracy_radius INTEGER,
    time_zone TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);