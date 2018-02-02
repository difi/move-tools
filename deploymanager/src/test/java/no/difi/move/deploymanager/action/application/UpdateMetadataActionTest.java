package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateMetadataActionTest {

    private UpdateMetadataAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private DeployDirectoryRepo repoMock;
    @Mock
    private Properties metaDataMock;
    @Mock
    private Application applicationMock;
    @Mock
    private ApplicationMetadata metadataMock;

    @Before
    public void setUp() throws Exception {
        when(repoMock.getMetadata()).thenReturn(metaDataMock);
        when(applicationMock.getLatest()).thenReturn(metadataMock);
        target = new UpdateMetadataAction(propertiesMock, repoMock);
    }

    @Test
    public void apply_successful_shouldSetMetadata() throws IOException {
        target.apply(applicationMock);
        verify(repoMock, Mockito.times(1)).setMetadata(metaDataMock);
    }

    @Test(expected = DeployActionException.class)
    public void apply_getMetaDataRaisesIOException_shouldThrow() throws IOException {
        when(repoMock.getMetadata()).thenThrow(IOException.class);
        target.apply(applicationMock);
    }

    @Test(expected = DeployActionException.class)
    public void apply_setMetaDataRaisesIOException_shouldThrow() throws IOException {
        doThrow(new IOException()).when(repoMock).setMetadata(metaDataMock);
        target.apply(applicationMock);
    }

    @Test(expected = IllegalStateException.class)
    public void apply_invalidApplicationMetadataReceived_shouldThrow() throws IOException {
        when(applicationMock.getLatest()).thenReturn(null);
        target.apply(applicationMock);
    }

    @Test(expected = IllegalStateException.class)
    public void apply_invalidDirectoryMetadataReceived_shouldThrow() throws IOException {
        when(repoMock.getMetadata()).thenReturn(null);
        target.apply(applicationMock);
    }
}
