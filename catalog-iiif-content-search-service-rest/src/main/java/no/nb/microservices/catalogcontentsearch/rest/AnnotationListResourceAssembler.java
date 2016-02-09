package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

public class AnnotationListResourceAssembler {

    public AnnotationList toResource(String id, String q, ContentSearchResult result) {
        ContentSearchResource contentSearchResult = result.getContentSearchResource();
        
        return new AnnotationListBuilder()
                .withId(id)
                .withQ(q)
                .withStruct(result.getStruct())
                .withFragments(contentSearchResult.getFragments())
                .withFreetextMetadatas(contentSearchResult.getFreetextMetadatas())
                .build();
    }

}
