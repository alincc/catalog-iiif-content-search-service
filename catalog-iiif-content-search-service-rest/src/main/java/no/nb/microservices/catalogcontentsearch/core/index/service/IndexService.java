package no.nb.microservices.catalogcontentsearch.core.index.service;

import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

public interface IndexService {

    SearchWithinResource searchWithin(String id, String q);
    
    
}