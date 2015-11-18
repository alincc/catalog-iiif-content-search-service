package no.nb.microservices.catalogsearchwithin.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
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

import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;
import no.nb.microservices.catalogsearchwithin.core.index.service.IndexService;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private IndexService indexService;
    
    @InjectMocks
    private SearchController controller;
    
    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/contentsearch/id1/search?q=searchwithin");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void testSearch() {
        SearchWithinResource searchWithinResource = createSearchIndexMock();
        given(indexService.searchWithin("id1", "searchwithin")).willReturn(searchWithinResource);
        
        AnnotationList annotationList = controller.search("id1", "searchwithin");
        
        assertThat(annotationList, equalTo(createExpectedAnnotationList()));
    }

    private SearchWithinResource createSearchIndexMock() {
        SearchWithinResource searchWithinResource = new SearchWithinResource();
        List<String> freetextMetadatas = new ArrayList<>();
        searchWithinResource.setFreetextMetadatas(freetextMetadatas);
        List<String> fragments = new ArrayList<>();
        searchWithinResource.setFragments(fragments);
        
        return searchWithinResource;
    }

    private AnnotationList createExpectedAnnotationList() {
        AnnotationList expected = new AnnotationList();
        expected.setContext("http://iiif.io/api/search/0/context.json");
        expected.setId("http://localhost/catalog/contentsearch/id1/search?q=searchwithin");
        expected.setType("sc:AnnotationList");
        return expected;
    }
    
}
