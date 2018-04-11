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
        DefaultAccessTokenRequest tokenRequest = new DefaultAccessTokenRequest();

        BaseOAuth2ProtectedResourceDetails resourceDetails = new BaseOAuth2ProtectedResourceDetails();
        resourceDetails.setAccessTokenUri(String.valueOf(properties.getOidc().getUrl().toURI()));
        resourceDetails.setScope(oidcTokenClient.getScopes());
        resourceDetails.setClientId(properties.getOidc().getClientId());

        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(tokenRequest));
        template.setAccessTokenProvider(new OidcAccessTokenProvider(oidcTokenClient));

        return template;
    }
}
