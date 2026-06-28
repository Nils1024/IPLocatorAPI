package de.nils.iplocatorapi.services;

import de.nils.iplocatorapi.daos.TLDData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class WhoisService {
    private static final Logger log = LoggerFactory.getLogger(WhoisService.class);

    public void whoisLookup(TLDData data) {
        String host = resolveWhoisHost(data);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, 43), 5_000);
            socket.setSoTimeout(5_000);
            socket.getOutputStream().write(
                    (data.getPunycode() + "\r\n").getBytes(StandardCharsets.UTF_8));
            String response = new String(
                    socket.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            parseWhoisResponse(response, data);
        } catch (IOException e) {
            log.error("WHOIS-Lookup failed for {} via {}: {}", data.getTld(), host, e.getMessage());
        }
    }

    private String resolveWhoisHost(TLDData data) {
        String url = data.getWhoisServer();
        if (url != null && !url.isBlank())
            return url.replaceFirst("^whois://", "").split("/")[0].trim();
        return "whois.iana.org";
    }

    private void parseWhoisResponse(String response, TLDData data) {
        for (String rawLine : response.split("\n")) {
            String line = rawLine.trim();
            if (line.startsWith("%") || line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String key   = line.substring(0, colon).trim().toLowerCase(Locale.ROOT);
            String value = line.substring(colon + 1).trim();
            if (value.isEmpty()) continue;
            switch (key) {
                case "organisation", "organization" -> { if (data.getRegistry() == null)    data.setRegistry(value); }
                case "type"                         -> { if (data.getType() == null)         data.setType(value.toLowerCase(Locale.ROOT)); }
                case "created"                      -> { if (data.getCreatedDate() == null)  data.setCreatedDate(value); }
                case "changed", "last-changed"      -> { if (data.getUpdatedDate() == null)  data.setUpdatedDate(value); }
                case "status"                       -> { if (data.getStatus() == null)       data.setStatus(value); }
                case "country"                      -> { if (data.getCountryCode() == null)  data.setCountryCode(value.toUpperCase(Locale.ROOT)); }
                case "whois"                        -> { if (data.getWhoisServer() == null)  data.setWhoisServer("whois://" + value); }
            }
        }
        if (data.getType() == null)
            data.setType(data.getCountryCode() != null ? "country-code" : "generic");
    }
}
