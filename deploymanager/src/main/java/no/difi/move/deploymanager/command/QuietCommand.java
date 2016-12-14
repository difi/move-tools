package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class QuietCommand implements AbstractCommand {

    @Override
    public void run(DeployManagerMain config) {
        config.getProperties().setProperty("quiet", "true");
    }

    @Override
    public boolean supports(CommandLine cmd) {
        return cmd.hasOption("quiet");
    }

    @Override
    public void commandLine(Options options) {
        options.addOption("quiet", "Disconnect after running");
    }

}
