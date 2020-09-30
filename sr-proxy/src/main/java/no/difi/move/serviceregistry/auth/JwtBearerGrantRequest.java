package no.difi.move.serviceregistry.auth;

import lombok.Getter;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
public class JwtBearerGrantRequest extends AbstractOAuth2AuthorizationGrantRequest {

    public static final AuthorizationGrantType JWT_BEARER_GRANT_TYPE =
            new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:jwt-bearer");

    private final ClientRegistration clientRegistration;

    public JwtBearerGrantRequest(ClientRegistration clientRegistration) {
        super(JWT_BEARER_GRANT_TYPE);
        this.clientRegistration = clientRegistration;
    }
}
