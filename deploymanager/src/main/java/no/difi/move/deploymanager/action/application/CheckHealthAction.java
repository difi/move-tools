package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.domain.application.Application;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class CheckHealthAction extends AbstractApplicationAction {

    public CheckHealthAction(Properties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application t) {
        log.debug("Running CheckHealthAction.");
        log.info("Performing health check.");
        try {
            Object content = new URL(getProperties().getProperty("healthURL")).getContent();
            t.setHealth(true);
        } catch (IOException ex) {
            log.warn(null, ex);
            t.setHealth(false);
        }
        return t;
    }

}
