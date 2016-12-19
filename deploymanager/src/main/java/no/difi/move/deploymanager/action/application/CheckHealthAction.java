package no.difi.move.deploymanager.action.application;

import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.domain.application.Application;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class CheckHealthAction extends AbstractApplicationAction {

    public CheckHealthAction(DeployManagerMain manager) {
        super(manager);
    }

    @Override
    public Application apply(Application t) {
        log.info("Shutdown running version.");
        try {
            Object content = new URL(getManager().getProperties().getProperty("healthURL")).getContent();
            t.setHealth(true);
        } catch (IOException ex) {
            log.warn(null, ex);
            t.setHealth(false);
        }
        return t;
    }

}
