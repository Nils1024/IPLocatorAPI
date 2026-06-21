package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.daos.IPData;
import de.nils.iplocatorapi.services.DataService;
import de.nils.iplocatorapi.utils.DomainUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.sql.SQLException;

@RequestMapping(Const.ApiPaths.DOMAIN)
@RestController
public class DomainController {
    private final DataService dataService;

    public DomainController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{domain}")
    public ResponseEntity<IPData> getDomain(@PathVariable String domain) {
        if(!DomainUtils.isValidDomain(domain)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(dataService.getIPData(DomainUtils.resolveIp(domain)));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        } catch (UnknownHostException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
