package no.difi.move.deploymanager.repo;

import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class NexusRepo {

    private final DeployManagerProperties properties;

    public NexusRepo(DeployManagerProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    public URL getArtifact(String version, String classifier)
            throws MalformedURLException {
        Assert.notNull(version, "version");
        Assert.notNull(classifier, "classifier");

        StringBuilder url = new StringBuilder();
        url.append(properties.getNexus());
        url.append("/service/local/artifact/maven/content?");
        ConcurrentHashMap<String, String> query = new ConcurrentHashMap<>();
        query.put("r", properties.getRepository());
        query.put("g", properties.getGroupId());
        query.put("a", properties.getArtifactId());
        query.put("v", version);
        if (classifier != null) {
            query.put("e", classifier);
        }
        String reduce = query.reduce(1, (k, v) -> k + "=" + v, (r1, r2) -> r1 + "&" + r2);
        url.append(reduce);
        return new URL(url.toString());
    }

}
