package no.nb.microservices.catalogcontentsearch.core.index.service;

import java.util.concurrent.Future;

import no.nb.microservices.catalogcontentsearch.core.search.TracableId;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

public interface IndexService {

    Future<ContentSearchResource> contentSearch(TracableId tracableId, String q);
    
}
