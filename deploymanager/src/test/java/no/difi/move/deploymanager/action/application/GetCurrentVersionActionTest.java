package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GetCurrentVersionActionTest {

    private GetCurrentVersionAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private DeployDirectoryRepo repoMock;
    @Mock
    private Properties repoMetadataMock;

    @Before
    public void setUp() throws Exception {
        when(repoMock.getMetadata()).thenReturn(repoMetadataMock);
        target = new GetCurrentVersionAction(propertiesMock, repoMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_calledOnNull_shouldThrow() {
        target.apply(null);
    }

    @Test(expected = DeployActionException.class)
    public void apply_getMetaDataThrowsIOException_shouldThrow() throws IOException {
        when(repoMock.getMetadata()).thenThrow(new IOException("test exception"));
        target.apply(new Application());
    }

    @Test(expected = IllegalStateException.class)
    public void apply_invalidDirectoryMetadataReceived_shouldThrow() throws IOException {
        when(repoMock.getMetadata()).thenReturn(null);
        target.apply(new Application());
    }

}
