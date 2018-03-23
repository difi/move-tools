package no.difi.move.eureka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@ConditionalOnProperty(name = "difi.move.auth.enable", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                requestMatchers().antMatchers("/config/**")
             .and().authorizeRequests().antMatchers(HttpMethod.GET, "/config/**").access("#oauth2.hasScope('move/config.read') or hasIpAddress('127.0.0.1')");


    }

}
