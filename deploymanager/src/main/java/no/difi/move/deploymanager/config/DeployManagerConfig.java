package no.difi.move.deploymanager.config;

import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeployManagerConfig {

    @Bean
    public DeployDirectoryRepo getDirectoryRepo(DeployManagerProperties properties){
        return new DeployDirectoryRepo(properties);
    }
}
