package no.difi.move.dashboard.config;

import java.net.InetAddress;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author kons-nlu
 */
@Data
@ConfigurationProperties(prefix = "difi.dashboard")
@Component
public class DashboardProperties {
    
    private Elasticsearch elasticsearch;

    @Data
    public static class Elasticsearch {
        @NotNull(message = "Set difi.dashboard.elasticsearch.host")
        private InetAddress host;
        @NotNull(message = "Set difi.dashboard.elasticsearch.port")
        private int port;
    }
    
}
