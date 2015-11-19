package no.nb.microservices.catalogcontentsearch.core.index.service;

import java.util.concurrent.Future;

import no.nb.microservices.catalogcontentsearch.core.search.service.TracableId;
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

public interface IndexService {

    Future<SearchWithinResource> contentSearch(TracableId tracableId, String q);
    
}
