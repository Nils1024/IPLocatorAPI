package de.nils.iplocatorapi.controllers;

import de.nils.iplocatorapi.common.Const;
import de.nils.iplocatorapi.security.RateLimitProtection;
import de.nils.iplocatorapi.services.DataService;
import de.nils.iplocatorapi.utils.DomainUtils;
import de.nils.iplocatorapi.utils.IPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import java.net.UnknownHostException;
import java.sql.SQLException;

@RequestMapping(Const.ApiPaths.RESOLVE)
@RestController
public class ResolveController {
    private final DataService dataService;

    public ResolveController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping()
    @RateLimitProtection
    public Object getResolve(@RequestParam String query) {
        try {
            if(IPUtils.isValidIp(query))
            {
                return dataService.getIPData(query);
            }
            else if(DomainUtils.isValidDomain(query))
            {
                return dataService.getDomainData(query);
            }
        } catch (UnknownHostException | NamingException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.notFound().build();
    }
}
