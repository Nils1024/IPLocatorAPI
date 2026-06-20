INSERT INTO asn (network, asn, organization)
VALUES %s
ON CONFLICT (network) do UPDATE
SET asn = EXCLUDED.asn,
    organization = EXCLUDED.organization;