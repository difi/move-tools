package no.difi.serviceregistry.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;

@Data
@Validated
@ConfigurationProperties("client")
public class ClientConfigurationProperties {

    /**
     * Service registry endpoint.
     */
    @NotNull(message = "Service registry endpoint must be configured")
    private URL endpointURL;

    @Valid
    private Oidc oidc;

    @Valid
    private SrSignature srSignature;

    @Data
    public static class SrSignature {
        @NotNull
        private String enabled; // This is of type String because of default property reference. (Shared with integrasjonspunkt.)
        private Resource certificate;
    }

    @Data
    public static class Oidc {
        @NotNull
        private String clientId;
        private URL url;
        @NestedConfigurationProperty
        private KeyStoreProperties keystore;
        private String audience;
        @NotNull
        private String scopes;
    }

}
