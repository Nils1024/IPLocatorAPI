package de.nils.iplocatorapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResolveController {

    @GetMapping("/v1/resolve")
    public String getResolve(@RequestParam String query) {
        return query;
    }
}
