package no.nb.microservices.catalogcontentsearch.core.search.service;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

public class ContentSearchResult {
    private final StructMap struct;
    private final ContentSearchResource contentSearchResource;
    
    public ContentSearchResult(StructMap struct,
            ContentSearchResource contentSearchResource) {
        this.struct = struct;
        this.contentSearchResource = contentSearchResource;
    }

    public StructMap getStruct() {
        return struct;
    }

    public ContentSearchResource getContentSearchResource() {
        return contentSearchResource;
    }

}
