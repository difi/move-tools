package no.difi.move.deploymanager.action.application;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.repo.DeployDirectoryRepo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class StartAction extends AbstractApplicationAction {

    private DeployDirectoryRepo directoryRepo;

    public StartAction(DeployManagerMain manager) {
        super(manager);
        this.directoryRepo = new DeployDirectoryRepo(manager);
    }

    @Override
    public Application apply(Application application) {
        if (application.getHealth() && application.getCurrent().getVersion().equals(application.getLatest().getVersion())) {
            return application;
        }
        log.info("Start application.");
        try {
            Process exec = Runtime.getRuntime().exec(
                    "java -jar " + application.getFile().getAbsolutePath() + " --endpoints.shutdown.enabled=true",
                    null,
                    new File(getManager().getProperties().getProperty("root")));

            getOutput(exec);

            updateMetadata(application);

            return application;
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Error starting application", ex);
        }
    }

    private void getOutput(Process exec) throws IOException {
        if (!getManager().getProperties().getOrDefault("quiet", "true").equals("true")) {
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
