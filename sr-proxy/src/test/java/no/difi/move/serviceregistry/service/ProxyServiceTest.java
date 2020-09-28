package no.difi.move.serviceregistry.service;

import no.difi.move.serviceregistry.client.ServiceRegistryClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProxyServiceTest {

    @Mock
    ServiceRegistryClient client;

    @InjectMocks
    ProxyService service;

    final static String ORG_NUMMER = "991825827";

    @Test
    public void shouldCallClientsLookupIdentifier() {
        when(client.lookupIdentifier(anyString())).thenReturn("hei");

        String response = service.lookupIdentifier(ORG_NUMMER);
        verify(client).lookupIdentifier(ORG_NUMMER);

        Assert.assertEquals("hei", response);
    }

}
