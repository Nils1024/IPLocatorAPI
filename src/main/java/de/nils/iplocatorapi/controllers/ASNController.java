package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(Const.ApiPaths.ASN)
@RestController
public class ASNController {
    private final DataService dataService;

    public ASNController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(path = "/{asn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAsn(@PathVariable String asn) {
        return dataService.getASNRdapData(asn);
    }
}
