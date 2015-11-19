package no.nb.microservices.catalogcontentsearch.core.search.service;

public interface ContentSearchService {

    ContentSearchResult search(String id, String q);
}
