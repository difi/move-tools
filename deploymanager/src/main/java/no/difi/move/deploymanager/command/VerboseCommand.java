package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class VerboseCommand implements AbstractCommand {

    private DeployManagerProperties properties;

    public VerboseCommand(DeployManagerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(DeployManagerMain config) {
        properties.setVerbose(true);
    }

    @Override
    public boolean supports(CommandLine cmd) {
        return cmd.hasOption("v");
    }

    @Override
    public void commandLine(Options options) {
        options.addOption("v", "verbose", false, "Follow application log.");
    }

}
