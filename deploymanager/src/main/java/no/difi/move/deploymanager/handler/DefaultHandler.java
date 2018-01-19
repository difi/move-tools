package no.difi.move.deploymanager.handler;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.application.*;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
@Component
public class DefaultHandler implements AbstractHandler {

    private DeployManagerProperties properties;

    public DefaultHandler(DeployManagerProperties properties) {
        this.properties = properties;
    }

    @Override
    @Scheduled(fixedRate = 120000)
    public void run() {
        log.debug("Starting synchronization.");
        new GetCurrentVersionAction(properties)
                .andThen(new LatestVersionAction(properties))
                .andThen(new PrepareApplicationAction(properties))
                .andThen(new ValidateAction(properties))
                .andThen(new CheckHealthAction(properties))
                .andThen(new ShutdownAction(properties))
                .andThen(new StartAction(properties))
                .apply(new Application());
        log.debug("Running StartAction.");
    }

}
