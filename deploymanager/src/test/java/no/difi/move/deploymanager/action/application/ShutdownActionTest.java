package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ShutdownAction.class})
public class ShutdownActionTest {

    private final URL urlMock;
    private final String NEW_APPLICATION_VERSION = "newVersion";
    private final String OLDER_APPLICATION_VERSION = "olderVersion";
    private ShutdownAction target;

    @Mock
    private DeployManagerProperties propertiesMock;

    public ShutdownActionTest() {
        this.urlMock = mock(URL.class);
    }

    @Before
    public void setUp() throws Exception {
        whenNew(URL.class).withParameterTypes(String.class)
                .withArguments(Mockito.anyString()).thenReturn(urlMock);
        when(propertiesMock.getShutdownURL()).thenReturn(urlMock);
        when(urlMock.openConnection()).thenReturn(mock(HttpURLConnection.class));
        target = new ShutdownAction(propertiesMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_applicationArgumentIsNull_shouldThrow() {
        target.apply(null);
    }

    @Test
    public void apply_currentVersionIsLatest_shouldSucceed() throws IOException {
        Application input = new Application();
        input.setHealth(true);
        ApplicationMetadata metadata = getMetadata(NEW_APPLICATION_VERSION);
        input.setLatest(metadata);
        input.setCurrent(metadata);

        target.apply(input);
    }

    @Test
    public void apply_currentVersionIsNotLatest_shouldSucceed() throws IOException {
        Application input = new Application();
        input.setHealth(true);
        input.setLatest(getMetadata(NEW_APPLICATION_VERSION));
        input.setCurrent(getMetadata(OLDER_APPLICATION_VERSION));
        when(urlMock.getContent()).thenReturn(Mockito.mock(Object.class));

        target.apply(input);
    }

    @Test
    public void apply_IOExceptionIsCaught_shouldNotThrow() throws IOException {
        Application input = new Application();
        input.setHealth(true);
        input.setLatest(getMetadata(NEW_APPLICATION_VERSION));
        input.setCurrent(getMetadata(OLDER_APPLICATION_VERSION));
        when(urlMock.getContent()).thenThrow(new IOException("test exception"));

        target.apply(input);
    }

    private ApplicationMetadata getMetadata(String version) {
        ApplicationMetadata metadata = mock(ApplicationMetadata.class);
        when(metadata.getVersion()).thenReturn(version);
        return metadata;
    }

}
