package no.difi.move.deploymanager.config;

import java.util.List;
import no.difi.move.deploymanager.command.AbstractCommand;
import org.apache.commons.cli.Options;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public final class CommandLineOptions {

    public final static Options options(List<AbstractCommand> commands) {
        Options options = new Options();

        for (AbstractCommand command : commands) {
            command.commandLine(options);
        }

        return options;
    }

}
