package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LatestVersionAction.class, IOUtils.class})
public class LatestVersionActionTest {

    private final URL urlMock;
    private final URLConnection connectionMock;
    @Mock
    DeployManagerProperties propertiesMock;
    private LatestVersionAction target;

    public LatestVersionActionTest() {
        urlMock = mock(URL.class);
        connectionMock = mock(URLConnection.class);
    }

    @Before
    public void setUp() throws Exception {
        when(urlMock.openConnection()).thenReturn(connectionMock);
        whenNew(URL.class).withParameterTypes(String.class)
                .withArguments(Mockito.anyString()).thenReturn(urlMock);
        when(propertiesMock.getNexusProxyURL()).thenReturn(urlMock);
        target = new LatestVersionAction(propertiesMock);
    }

    @Test(expected = NullPointerException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }

    @Test(expected = DeployActionException.class)
    public void apply_openConnectionThrowsIOException_shouldThrow() throws IOException {
        when(urlMock.openConnection()).thenThrow(new IOException("test"));
        Application result = target.apply(new Application());
        assertTrue(result.getHealth());
    }

    @Test(expected = DeployActionException.class)
    public void apply_raisesIOException_shouldThrow() throws IOException {
        when(connectionMock.getInputStream()).thenThrow(new IOException("test"));

        Application result = target.apply(new Application());
        assertTrue(result.getHealth());
    }

    @Test
    public void apply_receivesValidNexusResponse_shouldSetLatestVersion() throws IOException {
        final String baseVersion = "baseVersion";
        final String sha1 = "sha1";
        final String nexusResponse = getNexusResponse(baseVersion, sha1);
        InputStream streamMock = mock(InputStream.class);
        when(connectionMock.getInputStream()).thenReturn(streamMock);
        when(connectionMock.getContentEncoding()).thenReturn(null);
        mockStatic(IOUtils.class);
        when(IOUtils.toString(streamMock, connectionMock.getContentEncoding())).thenReturn(nexusResponse);

        Application result = target.apply(new Application());
        ApplicationMetadata latest = result.getLatest();

        Mockito.verify(streamMock).close();
        assertNotNull(latest);
        assertEquals(baseVersion, latest.getVersion());
        assertEquals(sha1, latest.getSha1());
    }

    private String getNexusResponse(String baseVersion, String sha1) {
        return "{\n" +
                "  \"baseVersion\": \"" + baseVersion + "\",\n" +
                "  \"version\": \"version\",\n" +
                "  \"sha1\": \"" + sha1 + "\",\n" +
                "  \"downloadUri\": \"https://downloadUri.here\"\n" +
                "}";
    }
}
