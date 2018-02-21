package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class CheckHealthAction extends AbstractApplicationAction {

    public CheckHealthAction(DeployManagerProperties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        Objects.requireNonNull(application);
        log.debug("Running CheckHealthAction.");
        try {
            log.info("Performing health check.");
            Object content = getProperties().getHealthURL().getContent();
            application.setHealth(true);
        } catch (IOException ex) {
            log.warn(null, ex);
            application.setHealth(false);
        }
        return application;
    }

}
