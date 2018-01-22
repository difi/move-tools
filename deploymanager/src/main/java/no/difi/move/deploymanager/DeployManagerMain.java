package no.difi.move.deploymanager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.command.AbstractCommand;
import no.difi.move.deploymanager.config.CommandLineOptions;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.config.DeployManagerPropertiesValidator;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(DeployManagerProperties.class)
public class DeployManagerMain implements CommandLineRunner {

    private final DeployManagerProperties deployManagerProperties;
    private CommandLine commandLine;
    private Options options;
    private List<AbstractCommand> commands;

    public DeployManagerMain(List<AbstractCommand> commands, DeployManagerProperties managerProperties) {
        this.commands = commands;
        this.deployManagerProperties = managerProperties;
    }

    @Bean(name = "configurationPropertiesValidator")
    public static Validator configurationPropertiesValidator() { // Required name and access modifier on custom Spring validator.
        return new DeployManagerPropertiesValidator();
    }

    public static void main(String[] args) {
        SpringApplication.run(DeployManagerMain.class, args);
    }

    @Override
    public void run(String... args) {
        parseCommands(args);
        runCommands();
    }

    private void parseCommands(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            this.options = CommandLineOptions.options(commands);
            this.commandLine = parser.parse(this.options, args);
        } catch (ParseException ex) {
            log.error(null, ex);
            System.exit(1);
        }
    }

    private void runCommands() {
        commands.stream()
                .filter((command) -> (command.supports(commandLine)))
                .forEachOrdered((command) -> command.run(this));
    }

}
