package no.difi.move.deploymanager.action.application;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateAction implements ApplicationAction {

    private final NexusRepo nexusRepo;

    @Override
    public Application apply(@NotNull Application application) {
        log.debug("Running ValidateAction.");
        try {
            log.info("Validating jar.");
            assertChecksumIsCorrect(application, ALGORITHM.SHA1);
            assertChecksumIsCorrect(application, ALGORITHM.MD5);
            return application;
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new DeployActionException("Error validating jar", ex);
        }
    }

    private void assertChecksumIsCorrect(Application application, ALGORITHM algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] hashFromRepo = getHashFromRepo(application.getLatest().getVersion(), algorithm);
        byte[] fileHash = getFileHash(application.getLatest().getFile(), algorithm);
        if (!MessageDigest.isEqual(fileHash, hashFromRepo)) {
            throw new DeployActionException(String.format("%s verification failed", algorithm.getName()));
        }
    }

    private byte[] getFileHash(File file, ALGORITHM algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[8192];
        MessageDigest instance = MessageDigest.getInstance(algorithm.getName());
        try (DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), instance)) {
            while (digestInputStream.read(buffer) != -1) ;
            return instance.digest();
        }
    }

    private byte[] getHashFromRepo(String applicationVersion, ALGORITHM algorithm) throws IOException {
        try (InputStream is = nexusRepo.getArtifact(applicationVersion, "jar." + algorithm.getFileNameSuffix()).openStream();
             StringWriter os = new StringWriter()) {
            IOUtils.copy(is, os, Charset.defaultCharset());
            return ByteArrayUtil.hexStringToByteArray(os.toString());
        }
    }

    @RequiredArgsConstructor
    @Getter
    private enum ALGORITHM {
        MD5("MD5", "md5"),
        SHA1("SHA-1", "sha1");

        private final String name;
        private final String fileNameSuffix;
    }
}
