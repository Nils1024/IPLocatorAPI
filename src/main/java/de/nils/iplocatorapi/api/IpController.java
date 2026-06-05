package de.nils.iplocatorapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {

    @GetMapping("/api/v1/ip")
    public String getIp() {
        return "127.0.0.1";
    }
}
