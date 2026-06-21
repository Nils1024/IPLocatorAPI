package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(Const.ApiPaths.BASE_PATH)
@RestController
public class GeneralController {
    private final DataService dataService;

    public GeneralController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/last-updated")
    public String getLastUpdated() {
        return "";
    }

    @GetMapping("/stats")
    public String getStats() {
        return "";
    }
}
