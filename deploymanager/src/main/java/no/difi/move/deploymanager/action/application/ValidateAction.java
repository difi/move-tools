package no.difi.move.deploymanager.action.application;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class ValidateAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public ValidateAction(DeployManagerProperties properties, NexusRepo nexusRepo) {
        super(properties);
        this.nexusRepo = nexusRepo;
    }

    @Override
    public Application apply(Application application) {
        Assert.notNull(application, "application");
        log.debug("Running ValidateAction.");
        log.info("Validating jar.");
        try {
            if (!verifyChecksum(application.getFile(), application.getLatest().getVersion(), ALGORITHM.SHA1)) {
                throw new DeployActionException("SHA-1 verification failed");
            }
            if (!verifyChecksum(application.getFile(), application.getLatest().getVersion(), ALGORITHM.MD5)) {
                throw new DeployActionException("MD-5 verification failed");
            }
            return application;
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new DeployActionException("Error validating jar", ex);
        }
    }

    private boolean verifyChecksum(File file, String version, ALGORITHM algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[8192];
        MessageDigest instance = MessageDigest.getInstance(algorithm.getName());

        try (
                InputStream is = nexusRepo.getArtifact(version, "jar." + algorithm.getFileNameSuffix()).openStream();
                StringWriter os = new StringWriter();
                DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), instance)) {

            IOUtils.copy(is, os, Charset.defaultCharset());

            while (digestInputStream.read(buffer) != -1) ;

            return MessageDigest.isEqual(instance.digest(), ByteArrayUtil.hexStringToByteArray(os.toString()));
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
