package no.nb.microservices.catalogcontentsearch;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogmetadata.test.struct.TestStructMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port:0")
public class SearchControllerIT {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer lb;

    RestTemplate template = new TestRestTemplate();
    
    MockWebServer server;
    
    @Before
    public void setup() throws Exception {

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {
            String searchid1Mock = IOUtils.toString(this.getClass().getResourceAsStream("id1.json"));
            String structMap = TestStructMap.structMapToString(TestStructMap.aDefaultStructMap().build());
            
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().startsWith("/catalog/v1/id1/search?q=test")) {
                    return new MockResponse().setBody(searchid1Mock).setHeader("Content-Type", "application/json; charset=utf-8");
                } else if (request.getPath().startsWith("/catalog/v1/metadata/id1/struct")) {
                    return new MockResponse().setBody(structMap).setHeader("Content-Type", "application/xml; charset=utf-8");
                }
                return new MockResponse().setResponseCode(404);
            }

        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }    
    
    @Test
    public void testSearch() {
        HttpHeaders headers = createDefaultHeaders();
        
        ResponseEntity<AnnotationList> response = new TestRestTemplate().exchange(
                "http://localhost:" + port + "/catalog/v1/contentsearch/id1/search?q=test", HttpMethod.GET,
                new HttpEntity<Void>(headers), AnnotationList.class);
        AnnotationList annotationList = response.getBody();
        
        assertTrue("Repsonse code should be successful", response.getStatusCode().is2xxSuccessful());
        assertNotNull("AnnotationList should not be null", annotationList);
        assertEquals("Should hava a context", "http://iiif.io/api/search/0/context.json", annotationList.getContext());
        assertEquals("Should have a type", "sc:AnnotationList", annotationList.getType());

    }
    
    private HttpHeaders createDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        return headers;
    }    

}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}