package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DomainController {
    private final DataService dataService;

    public DomainController(DataService dataService) {
        this.dataService = dataService;
    }
}
