package no.difi.move.deploymanager.action.application;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.handler.DefaultHandler;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class ShutdownAction extends AbstractApplicationAction {

    public ShutdownAction(DeployManagerMain manager) {
        super(manager);
    }

    @Override
    public Application apply(Application application) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    getManager().getProperties().getProperty("shutdownURL")).openConnection();
            connection.setRequestMethod("POST");
            connection.connect();
        } catch (IOException ex) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, null, ex);
        }
        return application;
    }

}
