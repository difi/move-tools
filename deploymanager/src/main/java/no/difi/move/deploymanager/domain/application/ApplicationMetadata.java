package no.difi.move.deploymanager.domain.application;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
@Builder
public class ApplicationMetadata {

    private final String repositoryId;
    private final String version;
    private final String sha1;

}
