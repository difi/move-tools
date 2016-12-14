package no.difi.move.deploymanager.action.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.repo.NexusRepo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class PrepareApplicationAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public PrepareApplicationAction(DeployManagerMain manager) {
        super(manager);
        this.nexusRepo = new NexusRepo(manager);
    }

    public Application apply(Application application) {
        log.info("Prepare jar.");
        String root = getManager().getProperties().getProperty("root");
        File download = new File(root, "integrasjonspunkt.jar");
        if (!application.getCurrent().getVersion().equals(application.getLatest().getVersion())) {
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
