package no.difi.move.eureka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(2)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.server.port}")
    private String managementPort;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println(managementPort);
        http
                .authorizeRequests()
                    .requestMatchers(request -> request.getLocalPort() == Integer.parseInt(managementPort))
                        .permitAll();
    }
}
