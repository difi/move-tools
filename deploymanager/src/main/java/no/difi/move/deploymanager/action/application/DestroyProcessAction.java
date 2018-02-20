package no.difi.move.deploymanager.action.application;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.predicate.ApplicationHealthPredicate;
import no.difi.move.deploymanager.util.ProcessIdFinder;
import org.springframework.util.Assert;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.ProcessUtil;
import org.zeroturnaround.process.Processes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DestroyProcessAction extends AbstractApplicationAction {

    private ProcessIdFinder processIdFinder;

    public DestroyProcessAction(DeployManagerProperties properties, ProcessIdFinder processIdFinder) {
        super(properties);
        this.processIdFinder = Objects.requireNonNull(processIdFinder);
    }

    @Override
    public Application apply(Application application) {
        Assert.notNull(application, "application");
        log.debug("Running DestroyProcessAction");

        List<String> processIds = processIdFinder.getPids(application.getFile().getName());
        if (new ApplicationHealthPredicate().negate().test(application)) {
            processIds.forEach(p -> {
                int pid = Integer.parseInt(p);
                doDestroyProcess(pid);
            });
        }

        return application;
    }

    private void doDestroyProcess(int pid) {
        log.info("Removing stalled legacy process {}.", pid);
        try {
            PidProcess process = Processes.newPidProcess(pid);
            if (process.isAlive()) {
                ProcessUtil.destroyForcefullyAndWait(process);
                log.info("Process removed.");
            }
        } catch (IOException e) {
            log.error("Could not destroy process {}", pid, e);
        } catch (InterruptedException e) {
            log.error("Destruction of process {} was interrupted", pid, e);
        }
    }

}
