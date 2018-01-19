package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class PrepareApplicationAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public PrepareApplicationAction(Properties properties) {
        super(properties);
        this.nexusRepo = new NexusRepo(properties);
    }

    public Application apply(Application application) {
        log.debug("Running PrepareApplicationAction.");
        log.info("Prepare jar.");
        String root = getProperties().getProperty("root");
        File download = new File(root, "integrasjonspunkt-" + application.getLatest().getVersion() + ".jar");
        if (!(download.exists() && new ApplicationVersionPredicate().test(application))) {
            log.info("Latest is different from current. Downloading newest version.");
            try {
                try (InputStream is = nexusRepo.getArtifact(application.getLatest().getVersion(), null).openStream();
                     OutputStream os = new FileOutputStream(download)) {
                    IOUtils.copy(is, os);
                }
            } catch (IOException ex) {
                log.error(null, ex);
                throw new DeployActionException("Error getting latest version", ex);
            }
        }
        application.setFile(download);
        return application;
    }

}
