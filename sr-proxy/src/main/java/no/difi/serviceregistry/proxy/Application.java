package no.difi.serviceregistry.proxy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        String port = System.getenv("SR_PORT");

        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", port != null ? Integer.parseInt(port) : 8080);

        new SpringApplicationBuilder()
                .sources(Application.class)
                .properties(props)
                .run(args);
    }
}
