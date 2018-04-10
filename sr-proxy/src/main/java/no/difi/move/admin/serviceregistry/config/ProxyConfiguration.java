package no.difi.move.admin.serviceregistry.config;

import no.difi.move.admin.serviceregistry.auth.OidcAccessTokenProvider;
import no.difi.move.admin.serviceregistry.auth.OidcTokenClient;
import no.difi.move.admin.serviceregistry.client.RestClient;
import no.difi.move.admin.serviceregistry.client.ServiceRegistryClient;
import no.difi.move.admin.serviceregistry.service.ProxyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;

@Configuration
@EnableOAuth2Client
public class ProxyConfiguration {

    @Bean
    RestClient restClient(ClientConfigurationProperties configurationProperties, RestOperations restOperations) throws CertificateException, IOException, URISyntaxException {
        return new RestClient(configurationProperties, restOperations);
    }

    @Bean
    ClientConfigurationProperties getClientConfigurationProperties() {
        return new ClientConfigurationProperties();
    }

    @Bean
    ServiceRegistryClient getServiceRegistryClient(RestClient restClient) throws URISyntaxException {
        return new ServiceRegistryClient(restClient);
    }

    @Bean
    ProxyService proxyService(ServiceRegistryClient restClient) {
        return new ProxyService(restClient);
    }

    @Bean
    OidcTokenClient getOidcTokenClient(ClientConfigurationProperties clientConfigurationProperties) {
        return new OidcTokenClient(clientConfigurationProperties);
    }

    @Bean
    public RestOperations getRestTemplate(ClientConfigurationProperties properties, OidcTokenClient oidcTokenClient) throws URISyntaxException {
        if (properties.getOidc().isEnabled()) {
            DefaultAccessTokenRequest atr = new DefaultAccessTokenRequest();
            BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
            resource.setAccessTokenUri(String.valueOf(properties.getOidc().getUrl().toURI()));
            resource.setScope(oidcTokenClient.getScopes());
            resource.setClientId(properties.getOidc().getClientId());

            OAuth2RestTemplate rt = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(atr));
            rt.setAccessTokenProvider(new OidcAccessTokenProvider(oidcTokenClient));
            return rt;
        }

        return new RestTemplate();
    }
}
