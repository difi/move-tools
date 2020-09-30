package no.difi.move.serviceregistry.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class JwtBearerOAuth2AuthorizedClientProvider implements OAuth2AuthorizedClientProvider {

    private final JwtBearerAccessTokenResponseClient tokenResponseClient;
    private Duration clockSkew = Duration.ofSeconds(60);
    private Clock clock = Clock.systemUTC();

    @Override
    public OAuth2AuthorizedClient authorize(OAuth2AuthorizationContext context) {
        log.debug("Attempts to authorize client assuming JWT bearer token grant type");
        Assert.notNull(context, "context cannot be null");

        ClientRegistration clientRegistration = context.getClientRegistration();

        if (!JwtBearerGrantRequest.JWT_BEARER_GRANT_TYPE.equals(clientRegistration.getAuthorizationGrantType())) {
            log.debug("Invalid grant type: {}", clientRegistration.getAuthorizationGrantType());
            return null;
        }

        OAuth2AuthorizedClient authorizedClient = context.getAuthorizedClient();
        if (null != authorizedClient && !isExpired(authorizedClient.getAccessToken())) {
            return null;
        }
        JwtBearerGrantRequest jwtBearerGrantRequest = new JwtBearerGrantRequest(clientRegistration);
        OAuth2AccessTokenResponse tokenResponse = tokenResponseClient.getTokenResponse(jwtBearerGrantRequest);

        return new OAuth2AuthorizedClient(clientRegistration, context.getPrincipal().getName(), tokenResponse.getAccessToken());
    }

    private boolean isExpired(AbstractOAuth2Token token) {
        return token.getExpiresAt().isBefore(Instant.now(clock).minus(clockSkew));
    }
}
