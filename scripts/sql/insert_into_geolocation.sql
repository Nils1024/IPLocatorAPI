INSERT INTO geolocations
(geoname_id, continent_code, country_code, region, city_name, time_zone)
VALUES %s
ON CONFLICT (geoname_id) DO UPDATE
SET continent_code = EXCLUDED.continent_code,
    country_code = EXCLUDED.country_code,
    region = EXCLUDED.region,
    city_name = EXCLUDED.city_name,
    time_zone = EXCLUDED.time_zone;