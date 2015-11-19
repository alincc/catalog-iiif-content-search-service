package no.nb.microservices.catalogcontentsearch.core.search.service;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

public class ContentSearchResult {
    private final StructMap struct;
    private final SearchWithinResource searchWithinResource;
    
    public ContentSearchResult(StructMap struct,
            SearchWithinResource searchWithinResource) {
        this.struct = struct;
        this.searchWithinResource = searchWithinResource;
    }

    public StructMap getStruct() {
        return struct;
    }

    public SearchWithinResource getSearchWithinResource() {
        return searchWithinResource;
    }

}
