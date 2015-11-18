package no.nb.microservices.catalogsearchwithin.core.index.service;

import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

public interface IndexService {

    SearchWithinResource searchWithin(String id, String q);
    
    
}
