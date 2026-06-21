package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

@RestController
public class IpController {
    private final DataService dataService;

    public IpController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/v1/ip/{ip}")
    public ResponseEntity<IPData> getIp(@PathVariable String ip) {
        if(!isValidIp(ip)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(dataService.getIPData(ip));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidIp(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch(UnknownHostException e) {
            return false;
        }
    }

    @GetMapping("/v1/ip/{ip}/location")
    public String getIpLocation(@PathVariable String ip) {
        return ip;
    }
}
