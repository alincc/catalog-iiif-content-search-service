package no.nb.microservices.catalogcontentsearch.rest;

import no.nb.microservices.catalogcontentsearch.rest.model.Annotation;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogcontentsearch.rest.model.Hit;
import no.nb.microservices.catalogmetadata.model.struct.Div;
import no.nb.microservices.catalogmetadata.model.struct.Resource;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.oletobias.metadatarepository.freetext.Document;
import no.nb.oletobias.metadatarepository.freetext.Page;
import no.nb.oletobias.metadatarepository.freetext.Rectangle;
import no.nb.oletobias.metadatarepository.freetext.Word;
import org.springframework.hateoas.Link;

import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

    public AnnotationListBuilder withFreetextMetadatas(List<String> freetextMetadatas) {
        this.freetextMetadatas = freetextMetadatas;
        return this;
    }
    
    public AnnotationList build() {
        AnnotationList annotationList = new AnnotationList();
        List<Hit> hits = new ArrayList<>();
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
            // Array with [0]=order, [1]=width, [2]=height
            fragments.stream().filter(fragment -> fragment != null).forEach(fragment -> {

                Hit hit = new Hit();
                hit.setBefore(buildBefore(fragment));
                hit.setAfter(buildAfter(fragment));
                hit.setAnnotations(Arrays.asList(id));
                hits.add(hit);
                annotationList.setHits(hits);

                Word word = Word.parse(fragment, 0);
                for (Rectangle wordRectangle : word.getPositionMetadata()) {
                    String pageName = metadataDocument.getPageNameFromPageID(wordRectangle.getPageId().getValue());
                    Page page = metadataDocument.getPageFromPageID(wordRectangle.getPageId().getValue());
                    // Array with [0]=order, [1]=width, [2]=height
                    String[] pageData = pageNameMap.get(pageName);
                    String pageUrn = pageData[4];
                    float resWidth = Float.parseFloat(pageData[1]);
                    float resHeight = Float.parseFloat(pageData[2]);

                    float factorX = resWidth / (float) page.getRectangle().getWidth().getValue();
                    float factorY = resHeight / (float) page.getRectangle().getHeight().getValue();

                    float x = (float) wordRectangle.getLeft().getValue();
                    float y = (float) wordRectangle.getTop().getValue();
                    float width = (float) wordRectangle.getWidth().getValue() * factorX;
                    float height = (float) wordRectangle.getHeight().getValue() * factorY;

                    int t = Math.round(y * factorY);
                    int l = Math.round(x * factorX);

                    Annotation annotation = new Annotation();
                    annotation.setId(id);
                    no.nb.microservices.catalogcontentsearch.rest.model.Resource resource = new no.nb.microservices.catalogcontentsearch.rest.model.Resource(q);
                    annotation.setResource(resource);
                    annotation.setOn(createOnLink(id, pageUrn, l, t, (int) width, (int) height).getHref());
                    annotationList.addResource(annotation);
                }
            });
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
            orderMap.put(div.getId(), new String[]{div.getOrder(), ""+resource.getWidth(), ""+resource.getHeight(), orderlabel, resource.getHref()});
        }
        return orderMap;
    }

    private String buildBefore(String fragment) {
        String before = "";
        String[] strings = fragment.split(" ");
        for (String string : strings) {
            Word word = Word.parse(string, 0);
            if (word.getText().toLowerCase().replace(".", "").replace(",", "").equalsIgnoreCase(q.toLowerCase())) {
                break;
            }
            before += word.getText() + " ";
        }

        return before;
    }

    private String buildAfter(String fragment) {
        String after = "";
        boolean isAfter = false;
        String[] strings = fragment.split(" ");
        for (String string : strings) {
            Word word = Word.parse(string, 0);
            if (isAfter) {
                after += word.getText() + " ";
            }
            if (word.getText().toLowerCase().replace(".", "").replace(",", "").equalsIgnoreCase(q.toLowerCase())) {
                isAfter = true;
            }
        }
        return after;
    }
}