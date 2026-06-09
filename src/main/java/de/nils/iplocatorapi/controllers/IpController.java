package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class IpController {
    private final DataService dataService;

    public IpController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/v1/ip/{ip}")
    public IPData getIp(@PathVariable String ip) {
        try {
            return dataService.getIPData(ip);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/v1/ip/{ip}/location")
    public String getIpLocation(@PathVariable String ip) {
        return ip;
    }
}
