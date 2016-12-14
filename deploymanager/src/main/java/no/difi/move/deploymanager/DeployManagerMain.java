package no.difi.move.deploymanager;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.command.AbstractCommand;
import no.difi.move.deploymanager.config.CommandLineOptions;
import no.difi.move.deploymanager.handler.AbstractHandler;
import no.difi.move.deploymanager.handler.DefaultHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
@Slf4j
public final class DeployManagerMain {

    private Properties properties = new Properties();
    private CommandLine commandLine;
    private Options options;
    private List<AbstractCommand> commands;
    private Class<? extends AbstractHandler> handler = DefaultHandler.class;

    public static void main(String[] args) throws Throwable {
        new DeployManagerMain(args).run();
    }

    private DeployManagerMain(final String[] args) {
        loadConfig();
        addCommands();
        parseCommandLine(args);
    }

    private void parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            this.options = CommandLineOptions.options(commands);
            this.commandLine = parser.parse(this.options, args);
        } catch (ParseException ex) {
            log.error(null, ex);
            System.exit(1);
        }
    }

    private void loadConfig() {
        try (InputStream is = new FileInputStream(new File("config.properties"))) {
            properties.load(is);
        } catch (IOException ex) {
            log.error("Can't load properties", ex);
        }
    }

    private void addCommands() {
        this.commands = new ArrayList();
        new FastClasspathScanner("no.difi.move.deploymanager.command")
                .matchClassesImplementing(AbstractCommand.class,
                        c -> {
                            try {
                                commands.add(c.newInstance());
                            } catch (InstantiationException | IllegalAccessException ex) {
                                log.error(null, ex);
                            }
                        }
                ).scan();
    }

    public void run() {
        commands.stream()
                .filter((command) -> (command.supports(commandLine)))
                .forEachOrdered((command) -> {
                    command.run(this);
                });
        try {
            handler.newInstance().run(this);
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error(null, ex);
        }
    }

}
