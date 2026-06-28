package de.nils.iplocatorapi.services;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.daos.ASNData;
import de.nils.iplocatorapi.daos.DomainData;
import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.daos.TLDData;
import de.nils.iplocatorapi.repository.DatabaseConnection;
import de.nils.iplocatorapi.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.net.IDN;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);
    private final DatabaseConnection dbConnection;
    private final List<String> resolvers = new ArrayList<>();
    private final String[] records = new String[]{
        "A",
        "AAAA",
        "MX",
        "TXT",
        "NS",
        "CNAME"
    };

    public DataService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;

        resolvers.add("dns://8.8.8.8/");
        resolvers.add("dns://1.1.1.1/");
        resolvers.add("dns://9.9.9.9/");
    }

    public IPData getIPData(String ip) throws SQLException {
        IPData ipData = new IPData();

        try(PreparedStatement statement = dbConnection.getConnection().prepareStatement(Const.SQL.IpDataQuery)) {
            statement.setString(1, ip);
            statement.setString(2, ip);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    ipData.setIp(ip);
                    ipData.setNetwork(resultSet.getString("network"));
                    ipData.setAsn(resultSet.getString("asn"));
                    ipData.setOrganization(resultSet.getString("organization"));
                    ipData.setContinent(resultSet.getString("continent_code"));
                    ipData.setCountry(resultSet.getString("country_code"));
                    ipData.setRegion(resultSet.getString("region"));
                    ipData.setCity(resultSet.getString("city_name"));
                    ipData.setPostalCode(resultSet.getString("postal_code"));
                    ipData.setTimezone(resultSet.getString("time_zone"));
                    ipData.setLatitude(resultSet.getDouble("latitude"));
                    ipData.setLongitude(resultSet.getDouble("longitude"));
                    ipData.setAccuracy(resultSet.getInt("accuracy_radius"));

                    Hashtable<String, String> env = new Hashtable<>();
                    env.put(Context.INITIAL_CONTEXT_FACTORY,
                            "com.sun.jndi.dns.DnsContextFactory");

                    DirContext ctx = new InitialDirContext(env);

                    Attributes attrs = ctx.getAttributes(
                            toArpa(ip),
                            new String[]{"PTR"});

                    Attribute ptrs = attrs.get("PTR");

                    List<String> hostnames = new ArrayList<>();

                    if (ptrs != null) {
                        for (int i = 0; i < ptrs.size(); i++) {
                            hostnames.add(ptrs.get(i).toString());
                        }
                    }

                    ipData.setHostnames(hostnames);
                }
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }

        return ipData;
    }

    public DomainData getDomainData(String domain) throws SQLException, UnknownHostException, NamingException {
        DomainData domainData = new DomainData();
        domainData.setDomain(domain);
        domainData.setIpData(getIPData(DomainUtils.resolveIp(domain)));

        for(String resolver : resolvers) {
            try {
                domainData.setRecords(getRecordsFromDNS(resolver, domain));
                break;
            }
            catch(NamingException e) {
                log.error("Failed to get DNS Records: ", e);
            }
        }

        return domainData;
    }

    private Map<String, List<String>> getRecordsFromDNS(String resolver, String domain) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.dns.DnsContextFactory"
        );
        env.put(Context.PROVIDER_URL, resolver);

        DirContext ctx = new InitialDirContext(env);

        Attributes attributes = ctx.getAttributes(domain, records);
        NamingEnumeration<? extends Attribute> allRecords =
                attributes.getAll();

        Map<String, List<String>> records = new HashMap<>();
        while(allRecords.hasMore()) {
            Attribute attribute = allRecords.next();

            for(int i = 0; i < attribute.size(); i++) {
                records.putIfAbsent(attribute.getID(), new ArrayList<>());
                records.get(attribute.getID()).add((String) attribute.get(i));
            }
        }

        return records;
    }

    private String getRdapIpData(String ip) {
        return makeHTTPRequest("https://rdap.org/ip/" + ip);
    }

    private String getWhoisData(String ip) {
        return ip;
    }

    public ASNData getASNData(String asn) {
        ASNData asnData = new ASNData();

        // makeHTTPRequest("https://rdap.apnic.net/autnum/" + asn);

        return asnData;
    }

    public TLDData getTLDData(String tld) {
        TLDData tldData = new TLDData();

        String normalized = tld.toLowerCase(Locale.ROOT).replaceAll("^\\.+", "").trim();
        tldData.setTld("." + normalized);
        tldData.setPunycode(IDN.toASCII(normalized));

        return tldData;
    }

    private String makeHTTPRequest(String uri) {
        try(HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build())
        {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                log.error("Request to <{}> failed: HTTP {}", uri, response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error requesting Data form <{}>: ", uri, e);
        }

        return null;
    }

    private String toArpa(String ip) {
        String[] parts = ip.split("\\.");

        return parts[3] + "." + parts[2] + "." + parts[1] + "." + parts[0] + ".in-addr.arpa";
    }
}
