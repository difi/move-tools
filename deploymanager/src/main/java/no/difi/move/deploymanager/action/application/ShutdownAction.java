package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationVersionPredicate;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class ShutdownAction extends AbstractApplicationAction {

    public ShutdownAction(DeployManagerProperties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        Assert.notNull(application, "application");
        log.debug("Running ShutdownAction.");
        if (needToShutdown(application)) {
            doShutdown();
        }

        return application;
    }

    private boolean needToShutdown(Application application) {
        return new ApplicationVersionPredicate().negate().and(new ApplicationHealthPredicate()).test(application);
    }

    private void doShutdown() {
        try {
            log.info("Shutdown running version.");
            HttpURLConnection connection = (HttpURLConnection) getProperties().getShutdownURL().openConnection();
            connection.setRequestMethod("POST");
            connection.getContent();
        } catch (IOException ex) {
            log.error(null, ex);
        }
    }

}

