package no.difi.move.deploymanager.command;

import no.difi.move.deploymanager.DeployManagerMain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public interface AbstractCommand {

    public void run(DeployManagerMain config);

    public boolean supports(CommandLine cmd);

    public void commandLine(Options options);
}
