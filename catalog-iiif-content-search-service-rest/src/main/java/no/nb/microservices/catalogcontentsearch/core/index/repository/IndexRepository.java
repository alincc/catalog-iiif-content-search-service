package no.nb.microservices.catalogcontentsearch.core.index.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

@FeignClient("catalog-search-index-service")
public interface IndexRepository {
    
    @RequestMapping(method = RequestMethod.GET, value = "/catalog/v1/{id}/search")
    ContentSearchResource contentSearch(@PathVariable("id") String id,
            @RequestParam("q") String q, 
            @RequestParam("X-Forwarded-Host") String xHost, 
            @RequestParam("X-Forwarded-Port") String xPort, 
            @RequestParam("X-Original-IP-Fra-Frontend") String xRealIp, 
            @RequestParam("amsso") String ssoToken);
    
}
