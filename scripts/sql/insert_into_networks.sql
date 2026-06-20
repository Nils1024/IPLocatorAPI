INSERT INTO networks (network, geoname_id, postal_code, latitude, longitude, accuracy_radius)
VALUES %s
ON CONFLICT (network) do UPDATE
SET geoname_id = EXCLUDED.geoname_id,
    postal_code = EXCLUDED.postal_code,
    latitude = EXCLUDED.latitude,
    longitude = EXCLUDED.longitude,
    accuracy_radius = EXCLUDED.accuracy_radius;