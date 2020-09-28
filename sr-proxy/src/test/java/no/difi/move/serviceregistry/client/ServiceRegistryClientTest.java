package no.difi.move.serviceregistry.client;

import com.nimbusds.jose.proc.BadJWSException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ServiceRegistryClientTest {

    private ServiceRegistryClient target;

    @Mock
    private RestClient restClientMock;

    @Before
    public void setUp() {
        target = new ServiceRegistryClient(restClientMock);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_restClientArgumentIsNull_shouldThrow() {
        target = new ServiceRegistryClient(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookupIdentifier_badJWSException_shouldThrow() throws BadJWSException {
        when(restClientMock.getResource(anyString())).thenThrow(new BadJWSException("test exception"));
        target.lookupIdentifier("organisasjonsnummer");
    }
}