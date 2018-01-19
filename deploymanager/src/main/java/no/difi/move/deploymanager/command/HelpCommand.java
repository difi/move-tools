package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class HelpCommand implements AbstractCommand {

    @Override
    public void run(DeployManagerMain config) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("deploymanager", config.getOptions());
        System.exit(1);
    }

    @Override
    public boolean supports(CommandLine cmd) {
        return cmd.hasOption("h");
    }

    @Override
    public void commandLine(Options options) {
        options.addOption("h", "help", false, "Display help");
    }
}
