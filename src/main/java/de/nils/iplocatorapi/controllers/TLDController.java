package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.web.bind.annotation.*;

@RequestMapping(Const.ApiPaths.TLD)
@RestController
public class TLDController {
    private final DataService dataService;

    public TLDController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{tld}")
    public String getTld(@PathVariable String tld) {
        return tld;
    }
}
