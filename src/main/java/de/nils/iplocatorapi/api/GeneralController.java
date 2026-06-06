package de.nils.iplocatorapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {
    @GetMapping("/v1/last-updated")
    public String getLastUpdated() {
        return "";
    }
}
