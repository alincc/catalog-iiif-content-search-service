package no.nb.microservices.catalogsearchwithin.rest;

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
