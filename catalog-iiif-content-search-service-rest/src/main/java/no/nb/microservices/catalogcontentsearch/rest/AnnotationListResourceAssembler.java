package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

public class AnnotationListResourceAssembler {

    public AnnotationList toResource(String id, String q, ContentSearchResult result) {
        SearchWithinResource contentSearchResult = result.getSearchWithinResource();
        
        return new AnnotationListBuilder()
                .withId(id)
                .withQ(q)
                .withStruct(result.getStruct())
                .withFragments(contentSearchResult.getFragments())
                .withFreetextMetadatas(contentSearchResult.getFreetextMetadatas())
                .build();
    }

}
