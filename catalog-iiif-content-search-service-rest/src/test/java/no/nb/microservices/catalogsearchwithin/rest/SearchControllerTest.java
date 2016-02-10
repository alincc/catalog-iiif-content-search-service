package no.nb.microservices.catalogsearchwithin.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchService;
import no.nb.microservices.catalogcontentsearch.rest.SearchController;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.microservices.catalogmetadata.test.struct.TestStructMap;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ContentSearchService contentSearchService;
    
    @InjectMocks
    private SearchController controller;
    
    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/contentsearch/id1/search?q=Oslo");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void testSearch() {
        ContentSearchResource contentSearchResource = createSearchIndexMock();
        StructMap struct = TestStructMap.aDefaultStructMap().build();
        ContentSearchResult contentSearchResult = new ContentSearchResult(struct, contentSearchResource);
        given(contentSearchService.search("id1", "Oslo")).willReturn(contentSearchResult);
        
        AnnotationList annotationList = controller.search("id1", "Oslo");
        
        assertThat(annotationList, equalTo(createExpectedAnnotationList()));
        assertEquals("Should get 4 hits", 4, annotationList.getHits().size());
        assertEquals("fornøjelse at byde jer velkommen til ", annotationList.getHits().get(0).getBefore());
        assertEquals("Nye og til musicalen Guys and Dolls. ", annotationList.getHits().get(0).getAfter());
        assertEquals("over at få tildelt. Som teaterchef på ", annotationList.getHits().get(1).getBefore());
        assertEquals("Nye nyder man det privelegium at komme ", annotationList.getHits().get(1).getAfter());

    }

    private ContentSearchResource createSearchIndexMock() {
        ContentSearchResource contentSearchResource = new ContentSearchResource();
        List<String> freetextMetadatas = Arrays.asList("DIV1#1000fNkW", "DIV2#1100fNkW", "DIV3#1200fNkW", "DIV4#1300fNkW",  "DIV5#1400fNkW");
        contentSearchResource.setFreetextMetadatas(freetextMetadatas);
        List<String> fragments = Arrays.asList(
                "fornøjelse#413K5À1v0A at#314Ú5Äsn byde#31555ÀÍA jer#315Ý5ÀFA velkommen#416l5À1S0r til#317Ø5Ànr Oslo#31805¿Ês Nye#318Ú5¿YA og#319B5ÈDr til#319Ù5Àor musicalen#41a25À1w0r Guys#31bQ5¿ÓA and#31cl5À[q Dolls.#31cÞ5¿Õr",
                "over#31có5üÄj at#310ù6Kso få#311y6Gts tildelt.#311Ë6HÝr Som#310ú6ÞÆs teaterchef#411Í6Þ1A0s på#312ý6ÜCB Oslo#313M6ÜÊt Nye#31496ÝYA nyder#314Ñ6ÝÙA man#315V6æ¿j det#31676ÝOs privelegium#416Ä6Ý1T0A at#318a6áso komme#318O6Þór",
                "af#31bÕ7jsr teater#31c17nÜn i#31cî7i3C alle#31d37jRr vægge#310ù7Ðìr og#311ó7ÑDq mure.#312G7ÏØj Oslo#313j7ÆÊs Nye#313ì7ÆZB har#314V7ÆNs stolte#314÷7ÇÖr traditioner#415Û7È1B0q som#317e7ÑÀh byens#317Þ7ÈÝz komedie#418Ë7È180q",
                "udfordrende#415x8N1Ç0s og#316ö8XDq moderne#417K8O1e0r måde.#318Ê8Nßs Jeg#310ø8æSz vil#311Z8ætq at#311è8êsm Oslo#312j8æÊq Nye#312í8æXz skal#313T8æZq være#31438îÏi et#314à8étn åbent,#315b8åáx inviterende#415ý8æ1N0q og#317Z8îDr"
        );
        contentSearchResource.setFragments(fragments);
        
        return contentSearchResource;
    }

    private AnnotationList createExpectedAnnotationList() {
        AnnotationList expected = new AnnotationList();
        expected.setContext("http://iiif.io/api/search/0/context.json");
        expected.setId("http://localhost/catalog/v1/contentsearch/id1/search?q=Oslo");
        expected.setType("sc:AnnotationList");
        return expected;
    }
    
}
