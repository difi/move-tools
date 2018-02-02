package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.*;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class PrepareApplicationAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public PrepareApplicationAction(DeployManagerProperties properties, NexusRepo nexusRepo) {
        super(properties);
        this.nexusRepo = nexusRepo;
    }

    public Application apply(Application application) {
        log.debug("Running PrepareApplicationAction.");
        Assert.notNull(application, "application");
        log.info("Prepare jar.");
        File downloadFile = getDownloadFile(application);
        if (shouldDownload(application, downloadFile)) {
            log.info("Latest is different from current. Downloading newest version.");
            try {
                doDownload(application, downloadFile);
            } catch (IOException ex) {
                throw new DeployActionException("Error getting latest version", ex);
            }
        }
        application.setFile(downloadFile);
        return application;
    }

    private void doDownload(Application application, File destination) throws IOException {
        try (InputStream is = nexusRepo.getArtifact(application.getLatest().getVersion(), null).openStream();
             OutputStream os = new FileOutputStream(destination)) {
            IOUtils.copy(is, os);
        }
    }

    private boolean shouldDownload(Application application, File fileToDownload) {
        boolean currentIsLatest = new ApplicationVersionPredicate().test(application);
        return !(fileToDownload.exists() && currentIsLatest);
    }

    private File getDownloadFile(Application application) {
        String root = getProperties().getRoot();
        String latestVersion = application.getLatest().getVersion();
        return new File(root, "integrasjonspunkt-" + latestVersion + ".jar");
    }

}
