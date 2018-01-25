package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class StartAction extends AbstractApplicationAction {

    private DeployDirectoryRepo directoryRepo;

    public StartAction(DeployManagerProperties properties) {
        super(properties);
        this.directoryRepo = new DeployDirectoryRepo(properties);
    }

    @Override
    public Application apply(Application application) {
        log.debug("Running StartAction.");
        if (new ApplicationHealthPredicate().and(new ApplicationVersionPredicate()).test(application)) {
            return application;
        }
        log.info("Start application.");
        try {
            String profile = getProperties().getIntegrasjonspunkt().getProfile();
            Process exec = Runtime.getRuntime().exec(
                    "java -jar "
                            + application.getFile().getAbsolutePath()
                            + " --endpoints.shutdown.enabled=true"
                            + String.format(" --spring.profiles.active=%s", profile),
                    null,
                    new File(getProperties().getRoot()));

            getOutput(exec);

            updateMetadata(application);

            return application;
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Error starting application", ex);
        }
    }

    private void getOutput(Process exec) throws IOException {
        if (getProperties().isVerbose()) {
            IOUtils.copy(exec.getInputStream(), System.out);
        }
    }

    private void updateMetadata(Application application) throws IOException {
        Properties metadata = directoryRepo.getMetadata();

        metadata.setProperty("version", application.getLatest().getVersion());
        if (application.getLatest().getSha1() != null) {
            metadata.setProperty("sha1", application.getLatest().getSha1());
        }
        metadata.setProperty("repositoryId", application.getLatest().getRepositoryId());

        directoryRepo.setMetadata(metadata);
    }

}
