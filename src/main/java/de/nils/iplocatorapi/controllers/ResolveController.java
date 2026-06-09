package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResolveController {
    private final DataService dataService;

    public ResolveController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/v1/resolve")
    public String getResolve(@RequestParam String query) {
        return query;
    }
}
