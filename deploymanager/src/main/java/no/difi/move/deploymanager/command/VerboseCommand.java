package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class VerboseCommand implements AbstractCommand {

    private DeployManagerProperties properties;

    public VerboseCommand(DeployManagerProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @Override
    public void run(DeployManagerMain config) {
        properties.setVerbose(true);
    }

    @Override
    public boolean supports(CommandLine cmd) {
        Assert.notNull(cmd, "cmd");
        return cmd.hasOption("v");
    }

    @Override
    public void commandLine(Options options) {
        Assert.notNull(options, "options");
        options.addOption("v", "verbose", false, "Follow application log.");
    }

}
