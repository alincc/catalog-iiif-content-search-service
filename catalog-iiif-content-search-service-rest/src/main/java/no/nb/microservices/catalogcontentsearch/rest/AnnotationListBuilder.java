package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;

public class AnnotationListBuilder {
    private String id;
    
    public AnnotationListBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public AnnotationList build() {
        AnnotationList annotationList = new AnnotationList();
        annotationList.setId(id);
        return annotationList;
    }
}
