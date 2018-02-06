package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class EnvironmentCommand implements AbstractCommand {

    private DeployManagerProperties properties;

    public EnvironmentCommand(DeployManagerProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @Override
    public void run(DeployManagerMain config) {
        properties.setEnvironment(config.getCommandLine().getOptionValue("e"));
    }

    @Override
    public boolean supports(CommandLine cmd) {
        return cmd.hasOption("e");
    }

    @Override
    public void commandLine(Options options) {
        options.addOption("e", "environment", true, "Add environment variables to application");
    }

}
