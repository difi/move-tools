package no.difi.move.admin.serviceregistry.client;

import com.nimbusds.jose.proc.BadJWSException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class ServiceRegistryClient {

    private RestClient restClient;

    @Autowired
    public ServiceRegistryClient(RestClient restClient) {
        this.restClient = Objects.requireNonNull(restClient);
    }

    public String lookupIdentifier(String identifier) {
        try {
            return restClient.getResource(identifier);
        } catch (BadJWSException e) {
            throw new IllegalStateException("Bad signature in service record response", e);
        }
    }
}
