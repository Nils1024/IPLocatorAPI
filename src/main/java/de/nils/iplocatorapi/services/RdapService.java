package de.nils.iplocatorapi.services;

import de.nils.iplocatorapi.daos.TLDData;
import de.nils.iplocatorapi.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RdapService {
    private static final Logger log = LoggerFactory.getLogger(RdapService.class);
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public boolean rdapLookup(TLDData data) {
        String url = "https://rdap.iana.org/domain/" + data.getPunycode();
        try {
            HttpResponse<String> response = httpClient.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Accept", "application/rdap+json")
                            .GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }

            String body = response.body();

            if (data.getIanaId() == null)
                JsonUtils.extractStringField(body, "handle", data::setIanaId);

            if (data.getStatus() == null)
                JsonUtils.extractFirstArrayString(body, "status").ifPresent(data::setStatus);

            if (data.getCreatedDate() == null || data.getUpdatedDate() == null)
                extractRdapEvents(body, data);

            if (data.getRegistry() == null)
                extractRdapRegistry(body, data);

            if (data.getType() == null)
                data.setType(data.getCountryCode() != null ? "country-code" : "generic");

            return true;

        } catch (IOException | InterruptedException e) {
            log.error("RDAP-Lookup failed for {}", data.getTld(), e);
            return false;
        }
    }

    private void extractRdapEvents(String json, TLDData data) {
        int eventsIdx = json.indexOf("\"events\"");
        if (eventsIdx < 0) return;
        int arrStart = json.indexOf('[', eventsIdx);
        int arrEnd   = JsonUtils.findClosing(json, arrStart, '[', ']');
        if (arrStart < 0 || arrEnd < 0) return;

        String arr = json.substring(arrStart, arrEnd + 1);
        int i = 0;
        while (i < arr.length()) {
            int objStart = arr.indexOf('{', i);
            if (objStart < 0) break;
            int objEnd = JsonUtils.findClosing(arr, objStart, '{', '}');
            if (objEnd < 0) break;

            String event  = arr.substring(objStart, objEnd + 1);
            String action = JsonUtils.extractRawString(event, "eventAction");
            String date   = JsonUtils.extractRawString(event, "eventDate");

            if (action != null && date != null) {
                String day = date.length() >= 10 ? date.substring(0, 10) : date;
                if ("registration".equalsIgnoreCase(action) && data.getCreatedDate() == null)
                    data.setCreatedDate(day);
                else if ("last changed".equalsIgnoreCase(action) && data.getUpdatedDate() == null)
                    data.setUpdatedDate(day);
            }
            i = objEnd + 1;
        }
    }

    private void extractRdapRegistry(String json, TLDData data) {
        int entIdx = json.indexOf("\"entities\"");
        if (entIdx < 0) return;
        int arrStart = json.indexOf('[', entIdx);
        int arrEnd   = JsonUtils.findClosing(json, arrStart, '[', ']');
        if (arrStart < 0 || arrEnd < 0) return;

        String arr = json.substring(arrStart, arrEnd + 1);
        int i = 0;
        while (i < arr.length()) {
            int objStart = arr.indexOf('{', i);
            if (objStart < 0) break;
            int objEnd = JsonUtils.findClosing(arr, objStart, '{', '}');
            if (objEnd < 0) break;

            String entity = arr.substring(objStart, objEnd + 1);
            if (entity.contains("\"registrar\"") || entity.contains("\"sponsor\"")) {
                String fn = extractVcardFn(entity);
                if (fn != null && data.getRegistry() == null)
                    data.setRegistry(fn);
            }
            i = objEnd + 1;
        }
    }

    private String extractVcardFn(String entityJson) {
        int fnIdx = entityJson.indexOf("\"fn\"");
        if (fnIdx < 0) return null;

        int pos = fnIdx + 4;
        int strCount = 0;

        while (pos < entityJson.length()) {
            char c = entityJson.charAt(pos);
            if (c == '{') {
                int depth = 1; pos++;
                while (pos < entityJson.length() && depth > 0) {
                    char ch = entityJson.charAt(pos++);
                    if (ch == '{') depth++; else if (ch == '}') depth--;
                }
            } else if (c == ']') {
                break;
            } else if (c == '"') {
                int end = pos + 1;
                while (end < entityJson.length() &&
                        (entityJson.charAt(end) != '"' || entityJson.charAt(end - 1) == '\\'))
                    end++;
                if (strCount == 1)
                    return entityJson.substring(pos + 1, end);
                strCount++;
                pos = end + 1;
            } else {
                pos++;
            }
        }
        return null;
    }
}
