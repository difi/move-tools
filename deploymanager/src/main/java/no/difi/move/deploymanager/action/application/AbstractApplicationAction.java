package no.difi.move.deploymanager.action.application;

import lombok.Data;
import no.difi.move.deploymanager.action.AbstractAction;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public abstract class AbstractApplicationAction implements AbstractAction<Application, Application> {

    private final DeployManagerProperties properties;

    public AbstractApplicationAction(DeployManagerProperties properties) {
        this.properties = properties;
    }

}
