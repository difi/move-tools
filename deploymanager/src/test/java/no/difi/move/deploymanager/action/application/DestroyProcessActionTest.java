package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.util.ProcessIdFinder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.ProcessUtil;
import org.zeroturnaround.process.Processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DestroyProcessAction.class, ProcessUtil.class, Processes.class})
public class DestroyProcessActionTest {

    private DestroyProcessAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private ProcessIdFinder processIdFinderMock;

    @Before
    public void setUp() throws Exception {
        target = new DestroyProcessAction(propertiesMock, processIdFinderMock);
        mockStatic(ProcessUtil.class);
        doNothing().when(ProcessUtil.class, "destroyForcefullyAndWait", any());
        mockStatic(Processes.class);
        PidProcess processMock = getProcessMock();
        when(Processes.newPidProcess(anyInt())).thenReturn(processMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }

    @Test(expected = DeployActionException.class)
    public void apply_finderThrows_shouldThrow() {
        Application applicationMock = getApplicationMock(true);
        when(processIdFinderMock.getPids(anyString())).thenThrow(new DeployActionException("test exception"));
        target.apply(applicationMock);
    }

    @Test
    public void apply_noProcessFoundRunning_nothingIsDestroyed() {
        Application applicationMock = getApplicationMock(true);
        when(processIdFinderMock.getPids(anyString())).thenReturn(new ArrayList<>());

        Application result = target.apply(applicationMock);

        verifyZeroInteractions(ProcessUtil.class);
        assertEquals(applicationMock, result);
    }

    @Test
    public void apply_healthyProcessFoundRunning_nothingIsDestroyed() {
        Application applicationMock = getApplicationMock(true);
        List<String> processIds = getProcessIdsMock();
        when(processIdFinderMock.getPids(anyString())).thenReturn(processIds);

        Application result = target.apply(applicationMock);

        verifyZeroInteractions(ProcessUtil.class);
        assertEquals(applicationMock, result);
    }

    @Test
    public void apply_unhealthyProcessFoundRunning_processIsDestroyed() {
        Application applicationMock = getApplicationMock(false);
        List<String> processIds = getProcessIdsMock();
        when(processIdFinderMock.getPids(anyString())).thenReturn(processIds);

        Application result = target.apply(applicationMock);
        verifyStatic(Mockito.times(1));

        assertEquals(applicationMock, result);
    }

    @Test
    public void apply_destroyProcessThrows_nothingIsDestroyed() throws Exception{
        Application applicationMock = getApplicationMock(false);
        List<String> processIds = getProcessIdsMock();
        when(processIdFinderMock.getPids(anyString())).thenReturn(processIds);
        doThrow(new IOException("test exception")).when(ProcessUtil.class, "destroyForcefullyAndWait", any());

        Application result = target.apply(applicationMock);

        assertEquals(applicationMock, result);
    }

    private PidProcess getProcessMock() throws IOException, InterruptedException {
        PidProcess processMock = mock(PidProcess.class);
        when(processMock.getPid()).thenReturn(0);
        when(processMock.isAlive()).thenReturn(true);
        return processMock;
    }

    private Application getApplicationMock(boolean isHealthy) {
        Application applicationMock = mock(Application.class);
        when(applicationMock.getHealth()).thenReturn(isHealthy);
        File fileMock = mock(File.class);
        when(fileMock.getName()).thenReturn("file.jar");
        when(applicationMock.getFile()).thenReturn(fileMock);
        return applicationMock;
    }

    private List<String> getProcessIdsMock() {
        List<String> processIds = new ArrayList<>();
        processIds.add("00000");
        return processIds;
    }
}
