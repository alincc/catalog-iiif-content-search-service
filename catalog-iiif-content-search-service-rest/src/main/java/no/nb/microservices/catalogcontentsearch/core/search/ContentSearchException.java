package no.nb.microservices.catalogcontentsearch.core.search;

public class ContentSearchException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ContentSearchException(String message, Throwable cause) {
        super(message, cause);
    }

}
