package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class GetCurrentVersionAction extends AbstractApplicationAction {

    private final DeployDirectoryRepo directoryRepo;

    public GetCurrentVersionAction(DeployManagerMain manager) {
        super(manager);
        this.directoryRepo = new DeployDirectoryRepo(manager);
    }

    @Override
    public Application apply(Application t) {
        log.info("Getting current version");
        try {
            Properties metadata = directoryRepo.getMetadata();
            t.setCurrent(ApplicationMetadata.builder().version(metadata.getProperty("version", "none")).build());
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Failed to get current version", ex);
        }
        return t;
    }

}
