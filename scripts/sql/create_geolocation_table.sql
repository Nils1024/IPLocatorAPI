CREATE TABLE IF NOT EXISTS geolocation (
    geoname_id INTEGER PRIMARY KEY,
    continent_code CHAR(2),
    country_code CHAR(2),
    region TEXT,
    city_name TEXT,
    time_zone TEXT
);