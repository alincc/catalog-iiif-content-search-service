package no.nb.microservices.catalogcontentsearch.core.search.service;

import no.nb.microservices.catalogcontentsearch.core.search.ContentSearchResult;

public interface ContentSearchService {

    ContentSearchResult search(String id, String q);
}
