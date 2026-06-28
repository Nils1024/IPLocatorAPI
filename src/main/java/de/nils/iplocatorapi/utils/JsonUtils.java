package de.nils.iplocatorapi.utils;

import java.util.Optional;
import java.util.function.Consumer;

public class JsonUtils {
    public static void extractStringField(String json, String fieldName, Consumer<String> setter) {
        String val = extractRawString(json, fieldName);
        if (val != null) setter.accept(val);
    }

    public static String extractRawString(String json, String fieldName) {
        int idx = json.indexOf("\"" + fieldName + "\"");
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        int q1 = json.indexOf('"', colon + 1);
        if (q1 < 0) return null;
        int q2 = q1 + 1;
        while (q2 < json.length() && (json.charAt(q2) != '"' || json.charAt(q2 - 1) == '\\'))
            q2++;
        return json.substring(q1 + 1, q2);
    }

    public static Optional<String> extractFirstArrayString(String json, String field) {
        int fieldIdx = json.indexOf("\"" + field + "\"");
        if (fieldIdx < 0) return Optional.empty();
        int bracket = json.indexOf('[', fieldIdx);
        if (bracket < 0) return Optional.empty();
        int q1 = json.indexOf('"', bracket + 1);
        if (q1 < 0) return Optional.empty();
        int q2 = q1 + 1;
        while (q2 < json.length() && (json.charAt(q2) != '"' || json.charAt(q2 - 1) == '\\'))
            q2++;
        return Optional.of(json.substring(q1 + 1, q2));
    }

    public static int findClosing(String s, int openIdx, char open, char close) {
        if (openIdx < 0 || openIdx >= s.length()) return -1;
        int depth = 0;
        for (int i = openIdx; i < s.length(); i++) {
            char c = s.charAt(i);
            if      (c == open)  depth++;
            else if (c == close) { if (--depth == 0) return i; }
        }
        return -1;
    }
}
