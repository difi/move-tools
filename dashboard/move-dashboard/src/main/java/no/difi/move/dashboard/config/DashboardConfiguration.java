package no.difi.move.dashboard.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author kons-nlu
 */
@Configuration
@EnableConfigurationProperties({DashboardProperties.class})
public class DashboardConfiguration {
    
}
