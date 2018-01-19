package no.difi.move.deploymanager.action.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class LatestVersionAction extends AbstractApplicationAction {

    public LatestVersionAction(Properties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        log.debug("Running LatestVersionAction.");
        log.info("Getting latest version");
        try {
            URLConnection connection = new URL(getProperties().getProperty("nexusProxyURL")).openConnection();
            String result = IOUtils.toString(connection.getInputStream(), connection.getContentEncoding());
            ApplicationMetadataDto dto = new ObjectMapper().readValue(result, ApplicationMetadataDto.class);
            application.setLatest(
                    ApplicationMetadata.builder()
                            .version(dto.getBaseVersion())
                            .repositoryId(getProperties().getProperty("repository", "staging"))
                            .sha1(dto.getSha1())
                            .build()
            );
            return application;
        } catch (IOException ex) {
            log.error(null, ex);
            throw new DeployActionException("Error downloading file", ex);
        }
    }

    @Data
    private static class ApplicationMetadataDto {

        private String baseVersion;
        private String version;
        private String sha1;
        private String downloadUri;
    }

}
