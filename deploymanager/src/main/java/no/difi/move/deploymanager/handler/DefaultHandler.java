package no.difi.move.deploymanager.handler;

import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.application.DownloadAction;
import no.difi.move.deploymanager.action.application.GetCurrentVersionAction;
import no.difi.move.deploymanager.action.application.LatestVersionAction;
import no.difi.move.deploymanager.action.application.ShutdownAction;
import no.difi.move.deploymanager.action.application.StartAction;
import no.difi.move.deploymanager.action.application.ValidateAction;
import no.difi.move.deploymanager.domain.Application;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class DefaultHandler implements AbstractHandler {

    @Override
    public void run(DeployManagerMain manager) {
        new GetCurrentVersionAction(manager)
                .andThen(new LatestVersionAction(manager))
                .andThen(new DownloadAction(manager))
                .andThen(new ValidateAction(manager))
                .andThen(new ShutdownAction(manager))
                .andThen(new StartAction(manager))
                .apply(new Application());
    }

}
