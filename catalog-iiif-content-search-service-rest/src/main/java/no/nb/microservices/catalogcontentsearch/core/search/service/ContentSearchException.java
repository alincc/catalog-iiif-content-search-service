package no.nb.microservices.catalogcontentsearch.core.search.service;

public class ContentSearchException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ContentSearchException(String message, Throwable cause) {
        super(message, cause);
    }

}
