package no.difi.move.deploymanager.action.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.repo.NexusRepo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class DownloadAction extends AbstractApplicationAction {

    private final NexusRepo nexusRepo;

    public DownloadAction(DeployManagerMain manager) {
        super(manager);
        this.nexusRepo = new NexusRepo(manager);
    }

    public Application apply(Application application) {
        if (application.getCurrent().getVersion().equals(application.getLatest().getVersion())) {
            return application;
        }
        String root = getManager().getProperties().getProperty("root");
        try {
            File download = new File(root, "integrasjonspunkt.jar");
            try (InputStream is = nexusRepo.getArtifact(application.getLatest().getVersion(), null).openStream();
                    OutputStream os = new FileOutputStream(download)) {
                IOUtils.copy(is, os);
                application.setFile(download);
                return application;
            }
        } catch (IOException ex) {
            Logger.getLogger(DownloadAction.class.getName()).log(Level.SEVERE, null, ex);
            throw new DeployActionException("Error getting latest version", ex);
        }
    }

}
