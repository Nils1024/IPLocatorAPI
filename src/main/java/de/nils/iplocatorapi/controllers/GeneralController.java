package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {
    private final DataService dataService;

    public GeneralController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/" + Const.version +  "/last-updated")
    public String getLastUpdated() {
        return "";
    }

    @GetMapping("/" + Const.version +  "/stats")
    public String getStats() {
        return "";
    }
}
