package no.difi.serviceregistry.proxy.service;

import no.difi.serviceregistry.proxy.client.ServiceRegistryClient;
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
