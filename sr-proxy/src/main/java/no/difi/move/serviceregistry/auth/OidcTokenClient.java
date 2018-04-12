package no.difi.move.serviceregistry.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.serviceregistry.config.ClientConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateEncodingException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
public class OidcTokenClient {

    private static List<String> scopes;
    private ClientConfigurationProperties properties;

    @Autowired
    public OidcTokenClient(ClientConfigurationProperties properties) {
        this.properties = Objects.requireNonNull(properties);
        scopes = Arrays.asList(properties.getOidc().getScopes().split(","));
    }

    public List<String> getScopes() {
        return scopes;
    }

    OidcTokenResponse fetchToken() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OidcErrorHandler());

        LinkedMultiValueMap<String, String> attrMap = new LinkedMultiValueMap<>();
        attrMap.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        attrMap.add("assertion", generateJWT());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(attrMap, headers);

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        restTemplate.getMessageConverters().add(formHttpMessageConverter);

        URI accessTokenUri;
        try {
            accessTokenUri = properties.getOidc().getUrl().toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Error converting property to URI", e);
        }

        ResponseEntity<OidcTokenResponse> response = restTemplate.exchange(accessTokenUri, HttpMethod.POST,
                httpEntity, OidcTokenResponse.class);
        log.info("Response: {}", response.toString());

        return response.getBody();
    }

    public String generateJWT() {
        KeystoreAccessor nokkel = new KeystoreAccessor(properties.getOidc().getKeystore());

        List<Base64> certChain = new ArrayList<>();
        try {
            certChain.add(Base64.encode(nokkel.getX509Certificate().getEncoded()));
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Could not get encoded certificate", e);
        }

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).x509CertChain(certChain).build();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .audience(properties.getOidc().getAudience())
                .issuer(properties.getOidc().getClientId())
                .claim("scope", String.join(" ", getScopes()))
                .jwtID(UUID.randomUUID().toString())
                .issueTime(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()))
                .expirationTime(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant().plusSeconds(120)))
                .build();

        JWSSigner signer = new RSASSASigner(nokkel.loadPrivateKey());
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claims);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            log.error("Error occured during signing of JWT", e);
        }

        String serializedJwt = signedJWT.serialize();
        log.info("SerializedJWT: {}", serializedJwt);

        return serializedJwt;
    }

}
