package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PrepareApplicationAction.class, IOUtils.class})
public class PrepareApplicationActionTest {

    private static final String NEW_APPLICATION_VERSION = "newVersion";
    private static final String OLDER_APPLICATION_VERSION = "olderVersion";
    private PrepareApplicationAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private NexusRepo nexusRepoMock;

    @Before
    public void setUp() {
        target = new PrepareApplicationAction(propertiesMock, nexusRepoMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }

    @Test
    public void apply_newVersionFound_shouldDownload() throws Exception {
        when(propertiesMock.getRoot()).thenReturn("");
        Application targetApplication = new Application();
        setLatestVersionToNew(targetApplication);
        setCurrentVersionToOlder(targetApplication);
        doSuccessfulDownload();

        Application result = target.apply(targetApplication);
        File resultFile = result.getFile();
        Assert.assertNotNull(resultFile);
        Assert.assertTrue(resultFile.getAbsolutePath().contains(NEW_APPLICATION_VERSION));
    }

    @Test(expected = DeployActionException.class)
    public void apply_downloadThrows_shouldThrow() throws Exception {
        when(propertiesMock.getRoot()).thenReturn("");
        Application targetApplication = new Application();
        setLatestVersionToNew(targetApplication);
        setCurrentVersionToOlder(targetApplication);
        getDownloadException();

        target.apply(targetApplication);
    }

    private void doSuccessfulDownload() throws Exception {
        URL urlMock = PowerMockito.mock(URL.class);
        InputStream streamMock = PowerMockito.mock(InputStream.class);
        when(urlMock.openStream()).thenReturn(streamMock);
        when(nexusRepoMock.getArtifact(Mockito.anyString(), Mockito.any())).thenReturn(urlMock);
        mockStatic(IOUtils.class);
        whenNew(FileOutputStream.class).withParameterTypes(File.class)
                .withArguments(null)
                .thenReturn(PowerMockito.mock(FileOutputStream.class));
        when(IOUtils.copy(Mockito.any(streamMock.getClass()), Mockito.any(OutputStream.class))).thenReturn(1);
    }

    private void getDownloadException() throws Exception {
        URL urlMock = PowerMockito.mock(URL.class);
        InputStream streamMock = PowerMockito.mock(InputStream.class);
        when(urlMock.openStream()).thenReturn(streamMock);
        when(nexusRepoMock.getArtifact(Mockito.anyString(), Mockito.any()))
                .thenThrow(new MalformedURLException("test download exception"));
    }

    private void setLatestVersionToNew(Application targetApplication) {
        ApplicationMetadata latestMetadataMock = PowerMockito.mock(ApplicationMetadata.class);
        when(latestMetadataMock.getVersion()).thenReturn(NEW_APPLICATION_VERSION);
        targetApplication.setLatest(latestMetadataMock);
    }

    private void setCurrentVersionToOlder(Application targetApplication) {
        ApplicationMetadata currentMetadataMock = PowerMockito.mock(ApplicationMetadata.class);
        when(currentMetadataMock.getVersion()).thenReturn(OLDER_APPLICATION_VERSION);
        targetApplication.setCurrent(currentMetadataMock);
    }
}
