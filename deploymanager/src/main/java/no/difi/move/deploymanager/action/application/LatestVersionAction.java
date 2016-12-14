package no.difi.move.deploymanager.action.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.Application;
import no.difi.move.deploymanager.domain.ApplicationMetadata;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class LatestVersionAction extends AbstractApplicationAction {

    public LatestVersionAction(DeployManagerMain manager) {
        super(manager);
    }

    @Override
    public Application apply(Application application) {
        log.info("Getting latest version");
        try {
            URLConnection connection = new URL("http://nexusproxy.azurewebsites.net/latest?env=staging").openConnection();
            String result = IOUtils.toString(connection.getInputStream(), connection.getContentEncoding());
            ApplicationMetadataDto dto = new ObjectMapper().readValue(result, ApplicationMetadataDto.class);
            application.setLatest(
                    ApplicationMetadata.builder()
                            .repositoryId(dto.getLatest().getRepositoryId())
                            .version(dto.getLatest().getBaseVersion()).build()
            );
            return application;
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Error downloading file", ex);
        }
    }

    @Data
    private static class ApplicationMetadataDto {

        private Latest latest;
    }

    @Data
    private static class Latest {

        private String repositoryId;
        private String baseVersion;
    }

}
