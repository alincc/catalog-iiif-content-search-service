package no.nb.microservices.catalogsearchwithin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;
import no.nb.microservices.catalogsearchwithin.core.index.service.IndexService;

@RestController
@RequestMapping(value = "/catalog/contentsearch")
public class SearchController {
    private IndexService indexService;
    
    @Autowired
    public SearchController(IndexService indexService) {
        super();
        this.indexService = indexService;
    }
    
    @RequestMapping(value = "/{id}/search")
    public AnnotationList search(@PathVariable String id, @RequestParam String q) {
        SearchWithinResource result = indexService.searchWithin(id, q);
        return new AnnotationListResourceAssembler().toResource(id, q, result);
    }

}