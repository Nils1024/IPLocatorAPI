package de.nils.iplocatorapi.services;

import de.nils.iplocatorapi.daos.DomainData;
import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.repository.DatabaseConnection;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class DataService {
    private final DatabaseConnection dbConnection;

    public DataService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public IPData getIPData(String ip) throws SQLException {
        IPData ipData = new IPData();

        try(PreparedStatement statement = dbConnection.getConnection().prepareStatement("""
                SELECT network, asn, organization
                FROM networks
                WHERE ?::inet <<= network
                LIMIT 1;
            """)) {

            statement.setString(1, ip);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    ipData.setNetwork(resultSet.getString("network"));
                    ipData.setAsn(resultSet.getString("asn"));
                    ipData.setOrganization(resultSet.getString("organization"));
                }
            }
        }

        return ipData;
    }

    public DomainData getDomainData(String domain) {
        return new DomainData();
    }
}
