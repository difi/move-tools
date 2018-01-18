package no.difi.move.deploymanager.handler;

import no.difi.move.deploymanager.action.application.*;
import no.difi.move.deploymanager.domain.application.Application;

import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class DefaultHandler implements AbstractHandler {

    @Override
    public void run(Properties properties) {
        new GetCurrentVersionAction(properties)
                .andThen(new LatestVersionAction(properties))
                .andThen(new PrepareApplicationAction(properties))
                .andThen(new ValidateAction(properties))
                .andThen(new CheckHealthAction(properties))
                .andThen(new ShutdownAction(properties))
                .andThen(new StartAction(properties))
                .apply(new Application());
    }

}
