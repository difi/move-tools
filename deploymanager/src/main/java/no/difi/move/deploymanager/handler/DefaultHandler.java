package no.difi.move.deploymanager.handler;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.application.*;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.repo.DeployDirectoryRepo;
import no.difi.move.deploymanager.repo.NexusRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
@Component
public class DefaultHandler implements AbstractHandler {

    private DeployManagerProperties properties;
    private DeployDirectoryRepo directoryRepo;
    private NexusRepo nexusRepo;

    public DefaultHandler(DeployManagerProperties properties, DeployDirectoryRepo repo, NexusRepo nexusRepo) {
        this.properties = properties;
        this.directoryRepo = repo;
        this.nexusRepo = nexusRepo;
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
                .andThen(new StartAction(properties))
                .andThen(new UpdateMetadataAction(properties, directoryRepo))
                .apply(new Application());
        log.debug("Finished synchronization.");
    }

}
