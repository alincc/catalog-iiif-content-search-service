package no.nb.microservices.catalogcontentsearch.core.search;

import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

public class ContentSearchResult {
    private final ContentSearchResource contentSearchResource;
    
    public ContentSearchResult(ContentSearchResource contentSearchResource) {
        this.contentSearchResource = contentSearchResource;
    }

    public ContentSearchResource getContentSearchResource() {
        return contentSearchResource;
    }

}
