package no.difi.move.serviceregistry.auth;

import com.nimbusds.jwt.SignedJWT;
import no.difi.move.serviceregistry.config.ClientConfigurationProperties;
import no.difi.move.serviceregistry.config.KeyStoreProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Ignore("Manual testing class")
public class OidcTokenClientTest {

    private ClientConfigurationProperties props;
    private List<String> scopes = Arrays.asList(
            "move/dpo.read",
            "move/dpe.read",
            "move/dpv.read",
            "move/dpi.read",
            "global/kontaktinformasjon.read",
            "global/sikkerdigitalpost.read",
            "global/varslingsstatus.read",
            "global/sertifikat.read",
            "global/navn.read",
            "global/postadresse.read");

    @Before
    public void setup() throws MalformedURLException {
        props = new ClientConfigurationProperties();
        props.setEndpointURL(new URL("http://localhost:9099/"));

        props.setOidc(new ClientConfigurationProperties.Oidc());
        props.getOidc().setUrl(new URL("https://oidc-ver2.difi.no/idporten-oidc-provider/token"));
        props.getOidc().setAudience("https://oidc-ver2.difi.no/idporten-oidc-provider/");
        props.getOidc().setClientId("test_move");
        props.getOidc().setScopes(scopes.stream().reduce((a, b) -> a + "," + b).get());
        props.getOidc().setKeystore(new KeyStoreProperties());
        props.getOidc().getKeystore().setAlias("client_alias");
        props.getOidc().getKeystore().setPassword("changeit");
        props.getOidc().getKeystore().setPath(new ClassPathResource("/kontaktinfo-client-test.jks"));
    }

    @Test
    public void testGenJWT() throws ParseException {
        OidcTokenClient oidcTokenClient = new OidcTokenClient(props);
        String jwt = oidcTokenClient.generateJWT();
        System.out.println(jwt);
        SignedJWT parsedJWT = SignedJWT.parse(jwt);
        assertEquals("test_move", parsedJWT.getJWTClaimsSet().getIssuer());
        assertEquals(scopes.stream().reduce((a, b) -> a + " " + b).get(), parsedJWT.getJWTClaimsSet().getClaims().get("scope"));
    }

}
