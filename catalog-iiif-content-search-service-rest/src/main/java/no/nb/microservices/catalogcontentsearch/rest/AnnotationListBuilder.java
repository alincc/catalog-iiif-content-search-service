package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.rest.model.Annotation;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogcontentsearch.rest.model.Hit;
import no.nb.microservices.catalogsearchindex.searchwithin.Fragment;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class AnnotationListBuilder {
    private String id;
    private String q;
    private List<Fragment> fragments;

    public AnnotationListBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public AnnotationListBuilder withQ(final String q) {
        this.q = q;
        return this;
    }

    public AnnotationListBuilder withFragments(List<Fragment> fragments) {
        this.fragments = fragments;
        return this;
    }

    public AnnotationList build() {
        AnnotationList annotationList = new AnnotationList();
        List<Hit> hits = new ArrayList<>();
        Link selfRel = linkTo(methodOn(SearchController.class).search(id, q)).withSelfRel();
        annotationList.setId(selfRel.getHref());
        for (Fragment fragment : fragments) {
            Hit hit = new Hit();
            hit.setAnnotations(Arrays.asList(id));
            hit.setBefore(fragment.getBefore());
            hit.setAfter(fragment.getAfter());
            hits.add(hit);

            Annotation annotation = new Annotation();
            annotation.setId(id);
            no.nb.microservices.catalogcontentsearch.rest.model.Resource resource = new no.nb.microservices.catalogcontentsearch.rest.model.Resource(fragment.getText());
            annotation.setResource(resource);
            annotation.setOn(createOnLink(id, fragment.getPageId(), fragment.getPosition().getX(), fragment.getPosition().getY(), fragment.getPosition().getWidth(), fragment.getPosition().getHeight()).getHref());
            annotationList.addResource(annotation);

        }
        annotationList.setHits(hits);
        return annotationList;
    }

    private Link createOnLink(String id, String canvasId, int x, int y, int w, int h) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.PRESENTATION, id, canvasId, x, y, w, h).withRel("on");
    }    
}