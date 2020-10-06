package no.difi.move.serviceregistry.oauth2;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.serviceregistry.config.ClientConfigurationProperties;
import no.difi.move.serviceregistry.keystore.KeystoreAccessor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.cert.CertificateEncodingException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtBearerAccessTokenResponseClient implements OAuth2AccessTokenResponseClient<JwtBearerGrantRequest> {

    private static final long EXPIRES_IN_SECONDS = 299;
    private final ClientConfigurationProperties properties;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(JwtBearerGrantRequest authorizationGrantRequest) {
        OidcTokenResponse tokenResponse = fetchToken(makeJwt());
        return OAuth2AccessTokenResponse.withToken(tokenResponse.getAccessToken())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(EXPIRES_IN_SECONDS)
                .scopes(Sets.newHashSet(properties.getOidc().getScopes()))
                .build();
    }

    private String makeJwt() {
        try {
            KeystoreAccessor accessor = new KeystoreAccessor(properties.getOidc().getKeystore());
            List<Base64> certificateChain = Lists.newArrayList(Base64.encode(accessor.getX509Certificate().getEncoded()));
            JWSHeader jwtHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .x509CertChain(certificateChain)
                    .build();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .audience(properties.getOidc().getAudience())
                    .issuer(properties.getOidc().getClientId())
                    .claim("scope", Lists.newArrayList(properties.getOidc().getScopes().split(",")))
                    .jwtID(UUID.randomUUID().toString())
                    .issueTime(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()))
                    .expirationTime(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant().plusSeconds(120)))
                    .build();
            JWSSigner signer = new RSASSASigner(accessor.getKeyPair().getPrivate());
            SignedJWT signedJWT = new SignedJWT(jwtHeader, claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException("Could not get encoded certificate", e);
        } catch (JOSEException e) {
            throw new IllegalStateException("Error occurred during signing of JWT", e);
        }
    }

    private OidcTokenResponse fetchToken(String jwt) {
        WebClient webClient = WebClient.builder()
                .baseUrl(properties.getOidc().getUrl().toString())
                .build();
        LinkedMultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("grant_type", JwtBearerGrantRequest.JWT_BEARER_GRANT_TYPE.getValue());
        requestParameters.add("assertion", jwt);
        return webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestParameters)
                .retrieve()
                .bodyToMono(OidcTokenResponse.class)
                .block();
    }

}
