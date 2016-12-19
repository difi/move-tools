package no.difi.move.deploymanager.action.application;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class ShutdownAction extends AbstractApplicationAction {

    public ShutdownAction(DeployManagerMain manager) {
        super(manager);
    }

    @Override
    public Application apply(Application application) {
        if (new ApplicationVersionPredicate().or(new ApplicationHealthPredicate().negate()).test(application)) {
            return application;
        }
        log.info("Shutdown running version.");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    getManager().getProperties().getProperty("shutdownURL")).openConnection();
            connection.setRequestMethod("POST");
            connection.connect();
        } catch (IOException ex) {
            log.error(null, ex);
        }
        return application;
    }

}
