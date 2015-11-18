package no.nb.microservices.catalogsearchwithin.core.index.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

@FeignClient("catalog-search-index-service")
public interface IndexRepository {
    
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/search")
    SearchWithinResource searchWithin(@PathVariable("id") String id, @RequestParam("q") String q);
}
