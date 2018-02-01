package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Properties;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class UpdateMetadataActionTest {

    private UpdateMetadataAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private DeployDirectoryRepo repoMock;

    @Before
    public void setUp() throws Exception {
        Mockito.when(repoMock.getMetadata()).thenReturn(Mockito.mock(Properties.class));
        target = new UpdateMetadataAction(propertiesMock, repoMock);
    }

    @Test
    public void name() {
        Application applicationMock = Mockito.mock(Application.class);
        target.apply(applicationMock);
    }
}
