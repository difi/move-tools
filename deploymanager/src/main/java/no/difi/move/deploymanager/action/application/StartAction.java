package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class StartAction extends AbstractApplicationAction {

    public StartAction(DeployManagerProperties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        Objects.requireNonNull(application);

        log.debug("Running StartAction.");
        if (isAlreadyRunning(application)) {
            log.info("The application is already running.");
            return application;
        }

        String startupProfile = getProperties().getIntegrasjonspunkt().getProfile();
        String jarPath = application.getFile().getAbsolutePath();
        Process exec = startProcess(jarPath, startupProfile);
        consumeOutput(exec);

        return application;
    }

    private boolean isAlreadyRunning(Application application) {
        return new ApplicationHealthPredicate().and(new ApplicationVersionPredicate()).test(application);
    }

    private Process startProcess(String jarPath, String activeProfile) {
        try {
            log.info("Starting application.");
            ProcessBuilder procBuilder = new ProcessBuilder(
                    "java", "-jar " + jarPath,
                    " --endpoints.shutdown.enabled=true --endpoints.health.enabled=true",
                    " --spring.profiles.active=" + activeProfile,
                    " --app.logger.enableSSL=false")
                    .directory(new File(getProperties().getRoot()));
            return procBuilder.start();
        } catch (IOException e) {
            throw new DeployActionException("Failed to start process.", e);
        }
    }

    private void consumeOutput(Process exec) {
        OutputStream outputStream = getOutputStream(getProperties().isVerbose());
        new Thread(() -> {
            try (InputStream inputStream = exec.getInputStream()) {
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException e) {
                throw new DeployActionException("Could not consume output stream.", e);
            }
        }).start();
    }

    private OutputStream getOutputStream(boolean preserveOutput) {
        return preserveOutput
                ? System.out
                : new OutputStream() {
            @Override
            public void write(int b) {

            }
        };
    }

}
