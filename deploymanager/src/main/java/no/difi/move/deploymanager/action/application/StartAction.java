package no.difi.move.deploymanager.action.application;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.repo.DeployDirectoryRepo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class StartAction extends AbstractApplicationAction {

    private DeployDirectoryRepo directoryRepo;

    public StartAction(DeployManagerMain manager) {
        super(manager);
        this.directoryRepo = new DeployDirectoryRepo(manager);
    }

    @Override
    public Application apply(Application application) {
        try {
            Process exec = Runtime.getRuntime().exec(
                    "java -jar integrasjonspunkt.jar --endpoints.shutdown.enabled=true",
                    null,
                    new File(getManager().getProperties().getProperty("root")));

            getOutput(exec);

            updateMetadata(application);

            return application;
        } catch (IOException ex) {
            Logger.getLogger(StartAction.class.getName()).log(Level.SEVERE, null, ex);
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
        metadata.setProperty("sha1", application.getLatest().getSha1());
        metadata.setProperty("repositoryId", application.getLatest().getRepositoryId());

        directoryRepo.setMetadata(metadata);
    }

}
