package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class GetCurrentVersionAction extends AbstractApplicationAction {

    private final DeployDirectoryRepo directoryRepo;

    public GetCurrentVersionAction(DeployManagerProperties properties, DeployDirectoryRepo directoryRepo) {
        super(properties);
        this.directoryRepo = Objects.requireNonNull(directoryRepo);
    }

    @Override
    public Application apply(Application application) {
        Objects.requireNonNull(application);
        log.debug("Running GetCurrentVersionAction.");
        try {
            log.info("Getting current version");
            Properties metadata = getDeployDirectoryMetadata();
            application.setCurrent(ApplicationMetadata.builder().version(metadata.getProperty("version", "none")).build());
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Failed to get current version", ex);
        }
        return application;
    }

    private Properties getDeployDirectoryMetadata() throws IOException {
        Properties properties = directoryRepo.getMetadata();
        if (null == properties) {
            throw new IllegalStateException(String.format("Invalid metadata encountered: %s.", properties));
        }

        return properties;
    }

}
