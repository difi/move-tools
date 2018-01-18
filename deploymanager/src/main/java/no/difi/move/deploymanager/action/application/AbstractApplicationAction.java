package no.difi.move.deploymanager.action.application;

import lombok.Data;
import no.difi.move.deploymanager.action.AbstractAction;
import no.difi.move.deploymanager.domain.application.Application;

import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public abstract class AbstractApplicationAction implements AbstractAction<Application, Application> {

    private final Properties properties;

    public AbstractApplicationAction(Properties properties) {
        this.properties = properties;
    }

}
