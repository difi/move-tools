package no.difi.move.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 *
 * @author nikko
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaServer
@EnableCaching
public class Eureka {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Eureka.class, args);
    }

}
