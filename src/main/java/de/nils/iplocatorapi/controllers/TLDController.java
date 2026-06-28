package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.security.RateLimitProtection;
import de.nils.iplocatorapi.services.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.regex.Pattern;

@RequestMapping(Const.ApiPaths.TLD)
@RestController
public class TLDController {
    private static final Pattern VALID_TLD = Pattern.compile("^[a-z0-9][a-z0-9\\-]*[a-z0-9]$|^[a-z0-9]$");

    private final DataService dataService;

    public TLDController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{tld}")
    @RateLimitProtection
    public ResponseEntity<Object> getTld(@PathVariable String tld) {
        if (tld == null || tld.isEmpty()) {
            return ResponseEntity.badRequest().body("TLD must not be empty");
        }
        
        String normalized = tld.replaceAll("^\\.+", "").trim();
        if (!VALID_TLD.matcher(normalized).matches()) {
            return ResponseEntity.badRequest().body("Invalid TLD: " + tld);
        }

        try {
            return ResponseEntity.ok().body(dataService.getTLDData(tld));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
