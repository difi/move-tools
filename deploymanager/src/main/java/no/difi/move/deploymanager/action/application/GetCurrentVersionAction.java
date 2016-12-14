package no.difi.move.deploymanager.action.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.ApplicationMetadata;
import no.difi.move.deploymanager.domain.repo.DeployDirectoryRepo;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class GetCurrentVersionAction extends AbstractApplicationAction {

    private final DeployDirectoryRepo directoryRepo;

    public GetCurrentVersionAction(DeployManagerMain manager) {
        super(manager);
        this.directoryRepo = new DeployDirectoryRepo(manager);
    }

    @Override
    public Application apply(Application t) {
        try {
            Properties metadata = directoryRepo.getMetadata();
            t.setCurrent(ApplicationMetadata.builder().version(metadata.getProperty("version", "none")).build());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetCurrentVersionAction.class.getName()).log(Level.SEVERE, null, ex);
            throw new DeployActionException("Failed to get current version", ex);
        } catch (IOException ex) {
            Logger.getLogger(GetCurrentVersionAction.class.getName()).log(Level.SEVERE, null, ex);
            throw new DeployActionException("Failed to get current version", ex);
        }
        return t;
    }

}
