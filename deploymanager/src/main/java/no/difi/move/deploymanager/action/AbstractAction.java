package no.difi.move.deploymanager.action;

import java.util.function.Function;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public interface AbstractAction<T, R> extends Function<T, R> {

}
