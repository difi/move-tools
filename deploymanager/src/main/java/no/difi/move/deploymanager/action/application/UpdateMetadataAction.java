package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class UpdateMetadataAction extends AbstractApplicationAction {

    private DeployDirectoryRepo directoryRepo;

    public UpdateMetadataAction(DeployManagerProperties properties, DeployDirectoryRepo directoryRepo) {
        super(properties);
        this.directoryRepo = directoryRepo;
    }

    @Override
    public Application apply(Application application) {
        log.debug("Running UpdateMetadataAction");
        try {
            Properties metadata = directoryRepo.getMetadata();
            metadata.setProperty("version", application.getLatest().getVersion());
            if (application.getLatest().getSha1() != null) {
                metadata.setProperty("sha1", application.getLatest().getSha1());
            }
            metadata.setProperty("repositoryId", application.getLatest().getRepositoryId());
            directoryRepo.setMetadata(metadata);

            return application;
        } catch (IOException e) {
            throw new DeployActionException("Could not update metadata.", e);
        }
    }

}
