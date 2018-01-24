package no.difi.move.deploymanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;

@Data
@Validated
@ConfigurationProperties(prefix = "deploymanager")
public class DeployManagerProperties {
    @NotNull
    private String root;
    @NotNull
    private URL nexus;
    @NotNull
    private String repository;
    @NotNull
    private String groupId;
    @NotNull
    private String artifactId;
    @NotNull
    private URL shutdownURL;
    @NotNull
    private URL healthURL;
    @NotNull
    private URL nexusProxyURL;
    private String environment = "";
    @NotNull
    private boolean verbose;

    @Valid
    private IntegrasjonspunktProperties integrasjonspunkt;

    @Data
    public static class IntegrasjonspunktProperties {
        @NotNull
        private String profile;
    }
}
