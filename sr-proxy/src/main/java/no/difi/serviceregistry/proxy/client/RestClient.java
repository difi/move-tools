package no.difi.serviceregistry.proxy.client;

import com.nimbusds.jose.proc.BadJWSException;
import no.difi.serviceregistry.proxy.config.ClientConfigurationProperties;
import no.difi.move.common.oauth.JWTDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static java.util.Arrays.asList;

/**
 * RestClient using simple HTTP requests to query services.
 */
public class RestClient {

    private final RestOperations restTemplate;
    private final URI baseUrl;
    private final boolean validateSrSignature;
    private final JWTDecoder jwtDecoder;
    private final PublicKey pk;

    @Autowired
    public RestClient(ClientConfigurationProperties clientConfigurationProperties, RestOperations restTemplate)
            throws IOException, URISyntaxException, CertificateException {
        this.restTemplate = restTemplate;
        this.baseUrl = clientConfigurationProperties.getEndpointURL().toURI();
        this.validateSrSignature = Boolean.parseBoolean(clientConfigurationProperties.getSrSignature().getEnabled());
        this.jwtDecoder = new JWTDecoder();
        this.pk = CertificateFactory.getInstance("X.509")
                .generateCertificate(clientConfigurationProperties.getSrSignature().getCertificate().getInputStream()).getPublicKey();
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

        if (validateSrSignature) {
            HttpHeaders headers = new HttpHeaders();
            headers.put("Accept", asList("application/jose, application/json"));
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);

            return jwtDecoder.getPayload(response.getBody(), pk);
        }

        return restTemplate.getForObject(uri, String.class);
    }

}
