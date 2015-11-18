package no.nb.microservices.catalogcontentsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;

import no.nb.htrace.annotation.EnableTracing;

@SpringBootApplication
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableOAuth2Resource
@RefreshScope
@EnableTracing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
