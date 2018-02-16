package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.util.ProcessIdFinder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class DestroyProcessActionTest {

    private DestroyProcessAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private ProcessIdFinder processIdFinderMock;

    @Before
    public void setUp() {
        target = new DestroyProcessAction(propertiesMock, processIdFinderMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }

    @Test(expected = DeployActionException.class)
    public void apply_finderThrows_shouldThrow() {
        Application applicationMock = mock(Application.class);
        File fileMock = mock(File.class);
        when(fileMock.getName()).thenReturn("file.jar");
        when(applicationMock.getFile()).thenReturn(fileMock);
        when(processIdFinderMock.getPids(anyString())).thenThrow(new DeployActionException("test exception"));
        target.apply(applicationMock);
    }
}
