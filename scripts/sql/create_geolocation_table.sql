CREATE TABLE IF NOT EXISTS geolocation (
    network CIDR PRIMARY KEY,
    country_code CHAR(2),
    city_name TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);