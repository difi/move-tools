package no.difi.move.deploymanager.action.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import no.difi.move.deploymanager.config.DeployManagerProperties;
import no.difi.move.deploymanager.domain.application.Application;
import no.difi.move.deploymanager.domain.application.ApplicationMetadata;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Slf4j
public class LatestVersionAction extends AbstractApplicationAction {

    public LatestVersionAction(DeployManagerProperties properties) {
        super(properties);
    }

    @Override
    public Application apply(Application application) {
        Objects.requireNonNull(application);
        log.debug("Running LatestVersionAction.");
        try {
            log.info("Getting latest version");
            URLConnection connection = getProperties().getNexusProxyURL().openConnection();
            InputStream inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream, connection.getContentEncoding());
            ApplicationMetadataDto dto = new ObjectMapper().readValue(result, ApplicationMetadataDto.class);
            application.setLatest(
                    ApplicationMetadata.builder()
                            .version(dto.getBaseVersion())
                            .repositoryId(getProperties().getRepository())
                            .sha1(dto.getSha1())
                            .build()
            );
            inputStream.close();
            return application;
        } catch (IOException ex) {
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
