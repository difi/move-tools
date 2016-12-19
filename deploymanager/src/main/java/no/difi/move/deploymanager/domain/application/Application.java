package no.difi.move.deploymanager.domain.application;

import java.io.File;
import lombok.Data;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public class Application {

    private File file;
    private Boolean health;
    private ApplicationMetadata latest;
    private ApplicationMetadata current;

}
