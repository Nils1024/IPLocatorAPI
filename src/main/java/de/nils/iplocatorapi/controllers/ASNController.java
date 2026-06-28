package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.daos.ASNData;
import de.nils.iplocatorapi.security.RateLimitProtection;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RequestMapping(Const.ApiPaths.ASN)
@RestController
public class ASNController {
    private final DataService dataService;

    public ASNController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(path = "/{asn}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimitProtection
    public ResponseEntity<ASNData> getAsn(@PathVariable String asn) {
        try {
            return ResponseEntity.internalServerError().body(dataService.getASNData(asn));
        } catch(SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
