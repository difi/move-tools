package no.difi.move.deploymanager;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.command.AbstractCommand;
import no.difi.move.deploymanager.config.CommandLineOptions;
import no.difi.move.deploymanager.handler.AbstractHandler;
import no.difi.move.deploymanager.handler.DefaultHandler;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
@Slf4j
@SpringBootApplication
public class DeployManagerMain implements CommandLineRunner{

    private Properties properties = new Properties();
    private CommandLine commandLine;
    private Options options;
    private List<AbstractCommand> commands;
    private Class<? extends AbstractHandler> handler = DefaultHandler.class;

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(DeployManagerMain.class, args);
    }

    private void initialize(String[] args) {
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
        try (InputStream is = new FileInputStream(getClass().getClassLoader().getResource("config.properties").getFile())) {
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

    public void run(String[] args) {
        initialize(args);
        runCommands();
        run();
    }

    private void run() {
        try {
            handler.newInstance().run(properties);
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error(null, ex);
        }
    }

    private void runCommands() {
        commands.stream()
                .filter((command) -> (command.supports(commandLine)))
                .forEachOrdered((command) -> command.run(this));
    }

}
