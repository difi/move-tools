package no.difi.serviceregistry.proxy.auth;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class OidcAccessTokenProvider implements AccessTokenProvider {

    private OidcTokenClient oidcTokenClient;

    @Autowired
    public OidcAccessTokenProvider(OidcTokenClient oidcTokenClient) {
        this.oidcTokenClient = Objects.requireNonNull(oidcTokenClient);
    }

    private DefaultOAuth2AccessToken getAccessToken() {
        OidcTokenResponse oidcTokenResponse = oidcTokenClient.fetchToken();
        DefaultOAuth2AccessToken oa2at = new DefaultOAuth2AccessToken(oidcTokenResponse.getAccessToken());
        oa2at.setExpiration(Date.from(Instant.now().plusSeconds(oidcTokenResponse.getExpiresIn())));
        oa2at.setScope(Sets.newHashSet(oidcTokenResponse.getScope()));
        return oa2at;
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest parameters) throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException {
        return getAccessToken();
    }

    @Override
    public boolean supportsResource(OAuth2ProtectedResourceDetails resource) {
        return false;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(OAuth2ProtectedResourceDetails resource, OAuth2RefreshToken refreshToken, AccessTokenRequest request) throws UserRedirectRequiredException {
        return getAccessToken();
    }

    @Override
    public boolean supportsRefresh(OAuth2ProtectedResourceDetails resource) {
        return false;
    }

}