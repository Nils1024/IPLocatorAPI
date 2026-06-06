INSERT INTO geolocation
(network, country_code, city_name, latitude, longitude)
VALUES (%s, %s, %s, %s, %s)
ON CONFLICT (network) DO UPDATE
SET country_code = EXCLUDED.country_code,
    city_name = EXCLUDED.city_name,
    latitude = EXCLUDED.latitude,
    longitude = EXCLUDED.longitude;