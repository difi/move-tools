package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class ShutdownAction extends AbstractApplicationAction {

    public ShutdownAction(Properties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        log.debug("Running ShutdownAction.");
        if (new ApplicationVersionPredicate().or(new ApplicationHealthPredicate().negate()).test(application)) {
            return application;
        }
        log.info("Shutdown running version.");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(getProperties().getProperty("shutdownURL")).openConnection();
            connection.setRequestMethod("POST");
            connection.getContent();
        } catch (IOException ex) {
            log.error(null, ex);
        }
        return application;
    }

}

