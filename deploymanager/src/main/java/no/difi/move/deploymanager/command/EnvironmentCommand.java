package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class EnvironmentCommand implements AbstractCommand {

    public EnvironmentCommand() {
    }

    @Override
    public void run(DeployManagerMain config) {
        config.getProperties().setProperty("environment", config.getCommandLine().getOptionValue("e"));
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
