package no.difi.move.deploymanager.domain.application.predicate;

import java.util.function.Predicate;
import no.difi.move.deploymanager.domain.application.Application;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class ApplicationVersionPredicate implements Predicate<Application> {

    @Override
    public boolean test(Application t) {
        return t.getCurrent().getVersion().equals(t.getLatest().getVersion());
    }

}
