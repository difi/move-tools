package no.difi.move.serviceregistry.service;

import no.difi.move.serviceregistry.client.ServiceRegistryClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class ProxyService {

    private final ServiceRegistryClient client;

    @Autowired
    public ProxyService(ServiceRegistryClient client) {
        this.client = Objects.requireNonNull(client);
    }

    public String lookupIdentifier(String identifier) {
        return client.lookupIdentifier(identifier);
    }
}
