package de.nils.iplocatorapi.common;

public class Const {
    public static class ApiPaths {
        public static final String BASE_PATH = "/v1";
        public static final String IP = BASE_PATH + "/ip";
        public static final String DOMAIN = BASE_PATH + "/domain";
    }

    public static class SQL {
        public static final String URL = "jdbc:postgresql://" + System.getenv("IPLocatorAPI_DB_URL") + ":5432/" + System.getenv("IPLocatorAPI_DB_NAME");
        public static final String IpDataQuery = """
                SELECT
                    n.network,
                    a.asn,
                    a.organization,
                    g.continent_code,
                    g.country_code,
                    g.region,
                    g.city_name,
                    n.postal_code,
                    g.time_zone,
                    n.latitude,
                    n.longitude,
                    n.accuracy_radius
                FROM networks n
                LEFT JOIN geolocations g USING(geoname_id)
                LEFT JOIN asn a ON inet(?::inet) <<= a.network
                WHERE inet(?::inet) <<= n.network
                ORDER BY masklen(n.network) DESC
                LIMIT 1;
            """;
    }
}
