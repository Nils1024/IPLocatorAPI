package de.nils.iplocatorapi.common;

public class Const {
    public static class SQL {
        public static final String url = "jdbc:postgresql://" + System.getenv("IPLocatorAPI_DB_URL") + ":5432";
    }
}
