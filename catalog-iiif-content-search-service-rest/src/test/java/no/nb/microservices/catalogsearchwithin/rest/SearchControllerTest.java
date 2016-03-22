package no.nb.microservices.catalogsearchwithin.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;

import no.nb.microservices.catalogsearchindex.searchwithin.Fragment;
import no.nb.microservices.catalogsearchindex.searchwithin.Position;
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

import no.nb.microservices.catalogcontentsearch.core.search.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.core.search.service.ContentSearchService;
import no.nb.microservices.catalogcontentsearch.rest.SearchController;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
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
        ContentSearchResult contentSearchResult = new ContentSearchResult(contentSearchResource);
        given(contentSearchService.search("id1", "Oslo")).willReturn(contentSearchResult);
        
        AnnotationList annotationList = controller.search("id1", "Oslo");
        
        assertThat(annotationList, equalTo(createExpectedAnnotationList()));
        assertThat("annotationlist contains 2 hits", annotationList.getHits().size(), is(2));
    }

    private ContentSearchResource createSearchIndexMock() {
        Fragment f1 = new Fragment("oslo","andreas jakter bjørn i", "med sprettert","pgid1",new Position(1,1,1,1));
        Fragment f2 = new Fragment("oslo","andreas jakter bjørn i", "med høygaffel","pgid2",new Position(2,2,2,2));
        return new ContentSearchResource(Arrays.asList(f1,f2));
    }

    private AnnotationList createExpectedAnnotationList() {
        AnnotationList expected = new AnnotationList();
        expected.setContext("http://iiif.io/api/search/0/context.json");
        expected.setId("http://localhost/catalog/v1/contentsearch/id1/search?q=Oslo");
        expected.setType("sc:AnnotationList");
        return expected;
    }
    
}
