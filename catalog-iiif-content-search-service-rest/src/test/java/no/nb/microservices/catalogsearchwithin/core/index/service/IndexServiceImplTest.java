package no.nb.microservices.catalogsearchwithin.core.index.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.htrace.Span;

import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogcontentsearch.core.index.repository.IndexRepository;
import no.nb.microservices.catalogcontentsearch.core.index.service.IndexServiceImpl;
import no.nb.microservices.catalogcontentsearch.core.search.service.SecurityInfo;
import no.nb.microservices.catalogcontentsearch.core.search.service.TracableId;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceImplTest {
    
    @Mock
    private IndexRepository indexRepository;
    
    @InjectMocks
    private IndexServiceImpl service;

    @Before
    public void setup() {
        mockRequest();
    }    
    
    @Test
    public void test() {
        SecurityInfo securityInfo = new SecurityInfo();
        TracableId tracableId = new TracableId(null, "id1", securityInfo);
        
        service.contentSearch(tracableId, "test");
        
        verify(indexRepository, times(1)).contentSearch(eq("id1"), eq("test"), any(), any(), any(), any());
    }

    private void mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");
        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        RequestContextHolder.setRequestAttributes(attributes);
    }
}
