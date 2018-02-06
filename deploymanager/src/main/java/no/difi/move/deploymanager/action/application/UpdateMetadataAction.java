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

@Slf4j
public class UpdateMetadataAction extends AbstractApplicationAction {

    private DeployDirectoryRepo directoryRepo;

    public UpdateMetadataAction(DeployManagerProperties properties, DeployDirectoryRepo directoryRepo) {
        super(properties);
        this.directoryRepo = Objects.requireNonNull(directoryRepo);
    }

    @Override
    public Application apply(Application application) {
        log.debug("Running UpdateMetadataAction");
        try {
            ApplicationMetadata applicationMetadata = getApplicationMetadata(application);
            Properties directoryProperties = getDirectoryProperties();

            directoryProperties.setProperty("version", applicationMetadata.getVersion());
            if (application.getLatest().getSha1() != null) {
                directoryProperties.setProperty("sha1", applicationMetadata.getSha1());
            }
            directoryProperties.setProperty("repositoryId", applicationMetadata.getRepositoryId());
            directoryRepo.setMetadata(directoryProperties);

            return application;
        } catch (IOException e) {
            throw new DeployActionException("Could not update metadata.", e);
        }
    }

    private Properties getDirectoryProperties() throws IOException {
        Properties properties = directoryRepo.getMetadata();
        if (null == properties) {
            throw new IllegalStateException("Invalid directory metadata encountered.");
        }
        return properties;
    }

    private ApplicationMetadata getApplicationMetadata(Application application) {
        ApplicationMetadata applicationMetadata = application.getLatest();
        if (null == applicationMetadata) {
            throw new IllegalStateException("Invalid application metadata encountered.");
        }
        return applicationMetadata;
    }

}
