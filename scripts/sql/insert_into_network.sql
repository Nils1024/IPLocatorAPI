INSERT INTO networks (network, asn, organization)
VALUES (%s, %s, %s)
ON CONFLICT (network) do UPDATE
SET asn = EXCLUDED.asn,
    organization = EXCLUDED.organization;