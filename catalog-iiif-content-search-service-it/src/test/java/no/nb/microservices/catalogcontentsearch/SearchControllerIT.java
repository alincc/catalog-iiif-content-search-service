package no.nb.microservices.catalogcontentsearch;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
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

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port:0")
public class SearchControllerIT {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer lb;

    MockWebServer server;

    @Before
    public void setup() throws Exception {
        server = new MockWebServer();
        String contentsearch = IOUtils.toString(this.getClass().getResourceAsStream("id1.json"));
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().startsWith("/catalog/v1/id1/search?q=teater")) {
                    return new MockResponse().setBody(contentsearch).setHeader("Content-Type", "application/json; charset=utf-8");
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
                "http://localhost:" + port + "/catalog/v1/contentsearch/id1/search?q=teater", HttpMethod.GET,
                new HttpEntity<Void>(headers), AnnotationList.class);
        AnnotationList annotationList = response.getBody();

        assertThat("Response code is 200", response.getStatusCode().value(), is(200));
        assertThat("AnnotationList has 19 hits", annotationList.getHits().size(), is(19));
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