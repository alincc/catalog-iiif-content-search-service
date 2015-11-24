package no.nb.microservices.catalogcontentsearch.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchService;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;

@RestController
@RequestMapping(value = "/v1/catalog/contentsearch")
public class SearchController {
    private ContentSearchService contentSearchService;
    
    @Autowired
    public SearchController(ContentSearchService contentSearchService) {
        super();
        this.contentSearchService = contentSearchService;
    }
    
    @RequestMapping(value = "/{id}/search")
    public AnnotationList search(@PathVariable String id, @RequestParam String q) {
        ContentSearchResult result = contentSearchService.search(id, q);
        return new AnnotationListResourceAssembler().toResource(id, q, result);
    }

}