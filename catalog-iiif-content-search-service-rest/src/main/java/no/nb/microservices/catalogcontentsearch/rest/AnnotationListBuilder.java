package no.nb.microservices.catalogcontentsearch.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;

import no.nb.microservices.catalogcontentsearch.rest.model.Annotation;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogmetadata.model.struct.Div;
import no.nb.microservices.catalogmetadata.model.struct.Resource;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.oletobias.metadatarepository.freetext.Document;
import no.nb.oletobias.metadatarepository.freetext.Page;
import no.nb.oletobias.metadatarepository.freetext.Rectangle;
import no.nb.oletobias.metadatarepository.freetext.Word;

public class AnnotationListBuilder {
    private String id;
    private String q;
    private List<String> fragments;
    private List<String> freetextMetadatas;
    private StructMap struct;
    
    public AnnotationListBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public AnnotationListBuilder withQ(String q) {
        this.q = q;
        return this;
    }
    
    public AnnotationListBuilder withStruct(StructMap struct) {
        this.struct = struct;
        return this;
    }
    
    public AnnotationListBuilder withFragments(List<String> fragments) {
        this.fragments = fragments;
        return this;
    }

    public AnnotationListBuilder withFreetextMedatas(List<String> freetextMetadatas) {
        this.freetextMetadatas = freetextMetadatas;
        return this;
    }
    
    public AnnotationList build() {
        AnnotationList annotationList = new AnnotationList();
        try {
        Link selfRel = linkTo(methodOn(SearchController.class).search(id, q)).withSelfRel();
        annotationList.setId(selfRel.getHref());
        
        StringBuilder metadataBuilder = new StringBuilder();
        for (String metadata : freetextMetadatas) {
            metadataBuilder.append(metadata + " ");
        }

        Document metadataDocument = Document.buildPageDocument(metadataBuilder.toString().trim());
        Map<String, String[]> pageNameMap = buildPageNameMapping();
        int i = 0;
        for (String fragment : fragments) {
            Word word = Word.parse(fragment, 0);
            for (Rectangle wordRectangle : word.getPositionMetadata()) {
                String pageName = metadataDocument.getPageNameFromPageID(wordRectangle.getPageId().getValue());
                Page page = metadataDocument.getPageFromPageID(wordRectangle.getPageId().getValue());
                // Array with [0]=order, [1]=width, [2]=height
                String[] pageData = pageNameMap.get(pageName);

                float resWidth = Float.parseFloat(pageData[1]);
                float resHeight = Float.parseFloat(pageData[2]);

                float factorX = resWidth / (float) page.getRectangle().getWidth().getValue();
                float factorY = resHeight / (float) page.getRectangle().getHeight().getValue();

                float x = (float) wordRectangle.getLeft().getValue();
                float y = (float) wordRectangle.getTop().getValue();
                float width = (float) wordRectangle.getWidth().getValue();
                float height = (float) wordRectangle.getHeight().getValue();

                int t = Math.round(y * factorY);
                int l = Math.round(x * factorX);
                int r = Math.round((x + width) * factorX);
                int b = Math.round((y + height) * factorY);
                if (i > 0) {
                    // @Todo line break?
                }
                System.out.println("t="+t);
                System.out.println("l="+l);
                System.out.println("r="+r);
                System.out.println("b="+b);
                Annotation annotation = new Annotation();
                annotation.setId(id);
                no.nb.microservices.catalogcontentsearch.rest.model.Resource resource = new no.nb.microservices.catalogcontentsearch.rest.model.Resource(q);
                annotation.setResource(resource);
                annotation.setOn(createOnLink(id, pageName, l, t, (int)width, (int)height).getHref());
                annotationList.addResource(annotation);
            }
        }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return annotationList;
    }

    private Link createOnLink(String id, String canvasId, int x, int y, int w, int h) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.PRESENTATION, id, canvasId, x, y, w, h).withRel("on");
    }    
    
    private Map<String, String[]> buildPageNameMapping() {
        Map<String, String[]> orderMap = new HashMap<String, String[]>();
        for(Div div : struct.getDivs()) {
            Resource resource = div.getResource();
            String orderlabel = "";
            if (div.getOrderLabel() != null && !div.getOrderLabel().isEmpty()) {
                   orderlabel = div.getOrderLabel();
            }            
            orderMap.put(div.getId(), new String[]{div.getOrder(), ""+resource.getWidth(), ""+resource.getHeight(), orderlabel});
        }
        return orderMap;
    }

}
