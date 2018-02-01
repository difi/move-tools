package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class CheckHealthAction extends AbstractApplicationAction {

    public CheckHealthAction(DeployManagerProperties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application t) {
        log.debug("Running CheckHealthAction.");
        Assert.notNull(t, "application");
        log.info("Performing health check.");
        try {
            Object content = getProperties().getHealthURL().getContent();
            t.setHealth(true);
        } catch (IOException ex) {
            log.warn(null, ex);
            t.setHealth(false);
        }
        return t;
    }

}
