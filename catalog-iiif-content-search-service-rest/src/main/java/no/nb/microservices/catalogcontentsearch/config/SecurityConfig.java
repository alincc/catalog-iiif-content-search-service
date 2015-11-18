package no.nb.microservices.catalogcontentsearch.config;

import no.nb.commons.web.sso.feign.SsoFeignInterceptor;
import no.nb.commons.web.xforwarded.feign.XForwardedParamsFeignInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({XForwardedParamsFeignInterceptor.class, SsoFeignInterceptor.class})
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
