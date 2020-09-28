package no.difi.move.serviceregistry.client;

import com.nimbusds.jose.proc.BadJWSException;
import no.difi.move.common.oauth.JWTDecoder;
import no.difi.move.serviceregistry.config.ClientConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertificateException;

import static java.util.Arrays.asList;

/**
 * RestClient using simple HTTP requests to query services.
 */
public class RestClient {

    private final RestOperations restTemplate;
    private final URI baseUrl;
    private final URL jwkUrl;
    private final JWTDecoder jwtDecoder;

    @Autowired
    public RestClient(ClientConfigurationProperties clientConfigurationProperties, RestOperations restTemplate)
            throws URISyntaxException, CertificateException {
        this.restTemplate = restTemplate;
        this.baseUrl = clientConfigurationProperties.getEndpointURL().toURI();
        this.jwkUrl = clientConfigurationProperties.getOidc().getJwkUrl();
        this.jwtDecoder = new JWTDecoder();
    }

    /**
     * Performs HTTP GET against baseUrl/resourcePath
     *
     * @param resourcePath which is resolved against baseUrl
     * @return response body
     */
    String getResource(String resourcePath) throws BadJWSException {
        return getResource(resourcePath, null);
    }

    /**
     * Performs HTTP GET against baseUrl/resourcePath
     *
     * @param resourcePath which is resolved against baseUrl
     * @return response body
     */
    String getResource(String resourcePath, String query) throws BadJWSException {
        URI uri = UriComponentsBuilder.fromUri(baseUrl).pathSegment(resourcePath).query(query).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.put("Accept", asList("application/jose, application/json"));
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);

        return jwtDecoder.getPayload(response.getBody(), jwkUrl);
    }

}
