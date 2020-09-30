package no.difi.move.serviceregistry.client;

import com.nimbusds.jose.proc.BadJWSException;
import lombok.RequiredArgsConstructor;
import no.difi.move.serviceregistry.auth.JwtDecoder;
import no.difi.move.serviceregistry.config.ClientConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WebClientServiceRegistryClient implements ServiceRegistryClient {

    private static final int BLOCK_DURATION_IN_SECONDS = 30;
    private final WebClient webClient;
    private final ClientConfigurationProperties properties;

    @Override
    public String lookupIdentifier(String identifier) {
        try {
            String response = webClient.get()
                    .uri("/{id}", identifier)
                    .header("Accept", "application/jose", "application/json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(BLOCK_DURATION_IN_SECONDS));
            return JwtDecoder.getPayload(response, properties.getOidc().getJwkUrl());
        } catch (BadJWSException e) {
            throw new IllegalStateException("JWT decoding failed", e);
        }
    }
}
