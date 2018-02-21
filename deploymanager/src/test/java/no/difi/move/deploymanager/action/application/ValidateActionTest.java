package no.difi.move.deploymanager.action.application;

import ch.qos.logback.core.encoder.ByteArrayUtil;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidateAction.class, IOUtils.class, MessageDigest.class, ByteArrayUtil.class})
public class ValidateActionTest {

    private ValidateAction target;

    @Mock
    private DeployManagerProperties propertiesMock;
    @Mock
    private NexusRepo nexusRepoMock;

    @Before
    public void setUp() {
        target = new ValidateAction(propertiesMock, nexusRepoMock);
    }

    @Test(expected = NullPointerException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }

    @Test(expected = DeployActionException.class)
    public void apply_verificationFails_shouldThrow() throws Exception {
        Application input = mockInputApplication();
        mockVerifyChecksum(false);
        target.apply(input);
    }

    @Test(expected = DeployActionException.class)
    public void apply_IOExceptionCaught_shouldThrow() throws Exception {
        Application input = mockInputApplication();
        mockInputStream();
        whenNew(StringWriter.class).withNoArguments().thenThrow(new IOException("test exception"));
        target.apply(input);
    }

    @Test(expected = DeployActionException.class)
    public void apply_NoSuchAlgorithmExceptionCaught_shouldThrow() throws Exception {
        Application input = mockInputApplication();
        mockInputStream();
        mockStatic(MessageDigest.class);
        when(MessageDigest.getInstance(Mockito.anyString())).thenThrow(new NoSuchAlgorithmException("test exception"));
        target.apply(input);
    }

    @Test
    public void apply_verificationSucceeds_shouldSucceed() throws Exception {
        Application input = mockInputApplication();
        mockVerifyChecksum(true);
        Application result = target.apply(input);
        Assert.assertNotNull(result);
    }

    private Application mockInputApplication() {
        File fileMock = mock(File.class);
        Application input = new Application();
        input.setFile(fileMock);
        ApplicationMetadata metadataMock = mock(ApplicationMetadata.class);
        when(metadataMock.getVersion()).thenReturn("version");
        input.setLatest(metadataMock);
        return input;
    }

    private void mockVerifyChecksum(boolean passVerification) throws Exception {
        mockInputStream();
        whenNew(StringWriter.class).withNoArguments().thenReturn(mock(StringWriter.class));

        FileInputStream fileInputStreamMock = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStreamMock);

        DigestInputStream digestInputStreamMock = mock(DigestInputStream.class);
        whenNew(DigestInputStream.class).withAnyArguments().thenReturn(digestInputStreamMock);
        when(digestInputStreamMock.read(Mockito.any())).thenReturn(-1);

        mockStatic(IOUtils.class);
        when(IOUtils.copy(Mockito.any(InputStream.class), Mockito.any(OutputStream.class))).thenReturn(1);

        mockStatic(MessageDigest.class);
        MessageDigest digestMock = mock(MessageDigest.class);
        when(MessageDigest.getInstance(Mockito.anyString())).thenReturn(digestMock);

        mockStatic(ByteArrayUtil.class);
        when(ByteArrayUtil.hexStringToByteArray(Mockito.anyString())).thenReturn(null);

        when(MessageDigest.isEqual(Mockito.any(), Mockito.any())).thenReturn(passVerification);
    }

    private void mockInputStream() throws IOException {
        URL urlMock = mock(URL.class);
        when(nexusRepoMock.getArtifact(Mockito.anyString(), Mockito.anyString())).thenReturn(urlMock);
        InputStream inputStreamMock = mock(InputStream.class);
        when(urlMock.openStream()).thenReturn(inputStreamMock);
    }
}
