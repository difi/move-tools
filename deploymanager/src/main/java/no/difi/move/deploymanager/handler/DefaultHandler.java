package no.difi.move.deploymanager.handler;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.application.*;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import no.difi.move.deploymanager.repo.NexusRepo;
import no.difi.move.deploymanager.util.ProcessIdFinder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
@Component
public class DefaultHandler implements AbstractHandler {

    private DeployManagerProperties properties;
    private DeployDirectoryRepo directoryRepo;
    private NexusRepo nexusRepo;
    private ProcessIdFinder processIdFinder;

    public DefaultHandler(DeployManagerProperties properties, DeployDirectoryRepo deployRepo, NexusRepo nexusRepo, ProcessIdFinder processIdFinder) {
        this.properties = Objects.requireNonNull(properties);
        this.directoryRepo = Objects.requireNonNull(deployRepo);
        this.nexusRepo = Objects.requireNonNull(nexusRepo);
        this.processIdFinder = Objects.requireNonNull(processIdFinder);
    }

    @Override
    @Scheduled(fixedRateString = "${deploymanager.schedulerFixedRateInMs}")
    public void run() {
        log.debug("Starting synchronization.");
        new GetCurrentVersionAction(properties, directoryRepo)
                .andThen(new LatestVersionAction(properties))
                .andThen(new PrepareApplicationAction(properties, nexusRepo))
                .andThen(new ValidateAction(properties, nexusRepo))
                .andThen(new CheckHealthAction(properties))
                .andThen(new ShutdownAction(properties))
                .andThen(new DestroyProcessAction(properties, processIdFinder))
                .andThen(new StartAction(properties))
                .andThen(new UpdateMetadataAction(properties, directoryRepo))
                .apply(new Application());
        log.debug("Finished synchronization.");
    }

}
