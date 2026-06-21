package de.nils.iplocatorapi.common;

public class Const {
    public static class ApiPaths {
        public static final String BASE_PATH = "/v1";
        public static final String IP = BASE_PATH + "/ip";
        public static final String DOMAIN = BASE_PATH + "/domain";
    }

    public static class SQL {
        public static final String URL = "jdbc:postgresql://" + System.getenv("IPLocatorAPI_DB_URL") + ":5432/" + System.getenv("IPLocatorAPI_DB_NAME");
    }
}
