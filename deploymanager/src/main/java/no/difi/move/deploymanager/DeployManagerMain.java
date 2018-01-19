package no.difi.move.deploymanager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.command.AbstractCommand;
import no.difi.move.deploymanager.config.CommandLineOptions;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(DeployManagerProperties.class)
public class DeployManagerMain implements CommandLineRunner {

    private Properties properties = new Properties();
    private CommandLine commandLine;
    private Options options;
    private List<AbstractCommand> commands;

    public DeployManagerMain(List<AbstractCommand> commands) {
        this.commands = commands;
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(DeployManagerMain.class, args);
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

    @Override
    public void run(String... args) {
        parseCommandLine(args);
        runCommands();
    }

    private void runCommands() {
        commands.stream()
                .filter((command) -> (command.supports(commandLine)))
                .forEachOrdered((command) -> command.run(this));
    }

}
