package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TLDController {
    private final DataService dataService;

    public TLDController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/v1/tld")
    public String getResolve(@RequestParam String tld) {
        return tld;
    }
}
