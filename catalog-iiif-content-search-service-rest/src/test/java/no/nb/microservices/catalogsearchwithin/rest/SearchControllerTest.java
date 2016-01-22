package no.nb.microservices.catalogsearchwithin.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
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
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ContentSearchService contentSearchService;
    
    @InjectMocks
    private SearchController controller;
    
    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/contentsearch/id1/search?q=searchwithin");
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
        StructMap struct = TestStructMap.aDefaultStructMap().build();
        ContentSearchResult contentSearchResult = new ContentSearchResult(struct, searchWithinResource);
        given(contentSearchService.search("id1", "searchwithin")).willReturn(contentSearchResult);
        
        AnnotationList annotationList = controller.search("id1", "searchwithin");
        
        assertThat(annotationList, equalTo(createExpectedAnnotationList()));
    }

    private SearchWithinResource createSearchIndexMock() {
        SearchWithinResource searchWithinResource = new SearchWithinResource();
        List<String> freetextMetadatas = Arrays.asList("DIV1#1000fNkW", "DIV2#1100fNkW", "DIV3#1200fNkW", "DIV4#1300fNkW",  "DIV5#1400fNkW");
        searchWithinResource.setFreetextMetadatas(freetextMetadatas);
        List<String> fragments = Arrays.asList("Lene#444Õ1ø100H");
        searchWithinResource.setFragments(fragments);
        
        return searchWithinResource;
    }

    private AnnotationList createExpectedAnnotationList() {
        AnnotationList expected = new AnnotationList();
        expected.setContext("http://iiif.io/api/search/0/context.json");
        expected.setId("http://localhost/catalog/v1/contentsearch/id1/search?q=searchwithin");
        expected.setType("sc:AnnotationList");
        return expected;
    }
    
}
