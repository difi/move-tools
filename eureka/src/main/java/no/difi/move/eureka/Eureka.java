package no.difi.move.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 *
 * @author nikko
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaServer
public class Eureka {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Eureka.class, args);
    }

}
