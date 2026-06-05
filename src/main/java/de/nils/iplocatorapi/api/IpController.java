package de.nils.iplocatorapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {

    @GetMapping("/v1/ip/{ip}")
    public String getIp(@PathVariable String ip) {
        return ip;
    }

    @GetMapping("/v1/ip/{ip}/location")
    public String getIpLocation(@PathVariable String ip) {
        return ip;
    }
}
