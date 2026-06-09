package de.nils.iplocatorapi.common;

public class Const {
    /**
     * Used for the API versioning in the endpoints
     */
    public static final String version = "v1";

    public static class SQL {
        public static final String url = "jdbc:postgresql://" + System.getenv("IPLocatorAPI_DB_URL") + ":5432";
    }
}
