package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.security.RateLimitProtection;
import de.nils.iplocatorapi.services.DataService;
import de.nils.iplocatorapi.utils.IPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

@RequestMapping(Const.ApiPaths.IP)
@RestController
public class IpController {
    private final DataService dataService;

    public IpController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{ip}")
    @RateLimitProtection
    public ResponseEntity<IPData> getIp(@PathVariable String ip) {
        if(!IPUtils.isValidIp(ip)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(dataService.getIPData(ip));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{ip}/location")
    public String getIpLocation(@PathVariable String ip) {
        return ip;
    }
}
