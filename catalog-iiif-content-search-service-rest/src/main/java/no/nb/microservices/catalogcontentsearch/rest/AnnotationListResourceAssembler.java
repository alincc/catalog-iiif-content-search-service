package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Link;

public class AnnotationListResourceAssembler {

    public AnnotationList toResource(String id, String q, SearchWithinResource result) {
        Link selfRel = linkTo(methodOn(SearchController.class).search(id, q)).withSelfRel();
        return new AnnotationListBuilder()
                .withId(selfRel.getHref())
                .build();
    }

}
