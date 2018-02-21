package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckHealthAction.class})
public class CheckHealthActionTest {

    private CheckHealthAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    private final URL urlMock;

    public CheckHealthActionTest() {
        urlMock = mock(URL.class);
    }

    @Before
    public void setUp() throws Exception {
        whenNew(URL.class).withParameterTypes(String.class)
                .withArguments(Mockito.anyString()).thenReturn(urlMock);
        when(propertiesMock.getHealthURL()).thenReturn(urlMock);
        target = new CheckHealthAction(propertiesMock);
    }

    @Test(expected = NullPointerException.class)
    public void apply_applicationArgumentIsNull_shouldThrow() {
        target.apply(null);
    }

    @Test
    public void apply_receivesContentFromUrl_shouldReturnHealthyResult() throws IOException {
        when(urlMock.getContent()).thenReturn(Mockito.mock(Object.class));
        Application result = target.apply(new Application());
        assertTrue(result.getHealth());
    }

    @Test
    public void apply_receivesIOException_shouldReturnUnhealthyResult() throws IOException {
        when(urlMock.getContent()).thenThrow(new IOException("test exception"));
        Application result = target.apply(new Application());
        assertFalse(result.getHealth());
    }

}
