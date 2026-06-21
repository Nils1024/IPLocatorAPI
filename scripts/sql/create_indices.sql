SELECT indexname, indexdef FROM pg_indexes
WHERE tablename IN ('networks', 'asn');

CREATE INDEX idx_networks_network ON networks USING GIST (network inet_ops);
CREATE INDEX idx_asn_network ON asn USING GIST (network inet_ops);