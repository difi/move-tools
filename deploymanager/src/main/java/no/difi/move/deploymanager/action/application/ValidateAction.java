package no.difi.move.deploymanager.action.application;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class ValidateAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public ValidateAction(DeployManagerProperties properties, NexusRepo nexusRepo) {
        super(properties);
        this.nexusRepo = Objects.requireNonNull(nexusRepo);
    }

    @Override
    public Application apply(Application application) {
        Objects.requireNonNull(application);
        log.debug("Running ValidateAction.");
        try {
            log.info("Validating jar.");
            if (!verifyChecksum(application, ALGORITHM.SHA1)) {
                throw new DeployActionException("SHA-1 verification failed");
            }
            if (!verifyChecksum(application, ALGORITHM.MD5)) {
                throw new DeployActionException("MD-5 verification failed");
            }
            return application;
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new DeployActionException("Error validating jar", ex);
        }
    }

    private boolean verifyChecksum(Application application, ALGORITHM algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] hashFromRepo = getHashFromRepo(application.getLatest().getVersion(), algorithm);
        byte[] fileHash = getFileHash(application.getFile(), algorithm);
        return MessageDigest.isEqual(fileHash, hashFromRepo);
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

    private enum ALGORITHM {
        MD5("MD5", "md5"),
        SHA1("SHA-1", "sha1");

        private final String name;
        private final String fileNameSuffix;

        private ALGORITHM(String name, String fileNameSuffix) {
            this.name = name;
            this.fileNameSuffix = fileNameSuffix;
        }

        public String getName() {
            return name;
        }

        public String getFileNameSuffix() {
            return fileNameSuffix;
        }
    }
}
