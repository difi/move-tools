package no.difi.move.deploymanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Data
@ConfigurationProperties(prefix = "no.difi.move.deploymanager")
public class DeployManagerProperties {
    private String root;
    private URL nexus;
    private String repository;
    private String groupId;
    private String artifactId;
    private URL shutdownURL;
    private URL healthURL;
    private URL nexusProxyURL;
    private String environment;
    private boolean verbose;
}
