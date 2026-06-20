package de.nils.iplocatorapi.services;

import de.nils.iplocatorapi.daos.DomainData;
import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.repository.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);
    private final DatabaseConnection dbConnection;

    public DataService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public IPData getIPData(String ip) throws SQLException {
        IPData ipData = new IPData();

        try(PreparedStatement statement = dbConnection.getConnection().prepareStatement("""
                SELECT n.network, a.asn, a.organization, g.country_code, g.city_name, n.latitude, n.longitude, r.abuse_email, r.registry
                FROM networks n
                LEFT JOIN geolocations g USING(geoname_id)
                LEFT JOIN asn a USING(network)
                LEFT JOIN rdap_networks r USING(network)
                WHERE inet(?::inet) <<= network
                ORDER BY masklen(n.network) DESC
                LIMIT 1;
            """)) {

            statement.setString(1, ip);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    ipData.setNetwork(resultSet.getString("network"));
                    ipData.setAsn(resultSet.getString("asn"));
                    ipData.setOrganization(resultSet.getString("organization"));
                    ipData.setCountry(resultSet.getString("country_code"));
                    ipData.setCity(resultSet.getString("city_name"));
                    ipData.setLatitude(resultSet.getDouble("latitude"));
                    ipData.setLongitude(resultSet.getDouble("longitude"));

                    if(resultSet.getString("registry") == null
                        || resultSet.getString("abuse_email") == null)
                    {
                        String json = getRdapIpData(ip);
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);

                        String network = ipData.getNetwork();
                        String organization = root.path("name").asText(null);
                        String handle = root.path("handle").asText(null);
                        String abuseEmail = extractAbuseEmail(root);

                        ipData.setAbuseEmail(abuseEmail);

                        try(PreparedStatement rdapInsertStatement = dbConnection.getConnection().prepareStatement("""
                                INSERT INTO rdap_networks (
                                    network,
                                    organization,
                                    handle,
                                    abuse_email,
                                    last_refresh
                                )
                                VALUES (?::CIDR, ?, ?, ?, NOW())
                                ON CONFLICT (network)
                                DO UPDATE SET
                                   organization = EXCLUDED.organization,
                                   handle = EXCLUDED.handle,
                                   abuse_email = EXCLUDED.abuse_email,
                                   last_refresh = NOW()
                            """)) {
                            rdapInsertStatement.setString(1, network);
                            rdapInsertStatement.setString(2, organization);
                            rdapInsertStatement.setString(3, handle);
                            rdapInsertStatement.setString(4, abuseEmail);

                            rdapInsertStatement.executeUpdate();
                        }
                    }
                }
            }
        }

        return ipData;
    }

    public DomainData getDomainData(String domain) {
        return new DomainData();
    }

    private String getRdapIpData(String ip) {
        try(HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build())
        {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://rdap.org/ip/" + ip)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                log.error("RDAP failed: HTTP {}", response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error getting RDAP Data for ip <{}>", ip, e);
        }

        return null;
    }

    public String extractAbuseEmail(JsonNode root) {
        JsonNode entities = root.path("entities");

        for(JsonNode entity : entities) {
            JsonNode roles = entity.path("roles");

            for(JsonNode role : roles) {
                if(role.asText().equals("abuse")) {
                    JsonNode vcard = entity.path("vcardArray");

                    if(vcard.isArray() && vcard.size() > 1) {
                        JsonNode fields = vcard.get(1);

                        for(JsonNode field : fields) {
                            if(field.get(0).asText().equals("email")) {
                                return field.get(3).asText();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
