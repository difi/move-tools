package no.difi.move.deploymanager.domain;

import java.io.File;
import lombok.Data;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public class Application {

    private File file;
    private ApplicationMetadata latest;
    private ApplicationMetadata current;

}
