package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class HelpCommand implements AbstractCommand {

    @Override
    public void run(DeployManagerMain config) {
        Assert.notNull(config, "config");
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("deploymanager", config.getOptions());
        System.exit(1);
    }

    @Override
    public boolean supports(CommandLine cmd) {
        Assert.notNull(cmd, "cmd");
        return cmd.hasOption("h");
    }

    @Override
    public void commandLine(Options options) {
        Assert.notNull(options, "options");
        options.addOption("h", "help", false, "Display help");
    }
}
