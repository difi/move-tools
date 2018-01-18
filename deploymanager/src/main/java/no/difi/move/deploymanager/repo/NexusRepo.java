package no.difi.move.deploymanager.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class NexusRepo {

    private final Properties properties;

    public NexusRepo(Properties properties) {
        this.properties = properties;
    }

    public URL getArtifact(String version, String classifier)
            throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        url.append(properties.getProperty("nexus", "https://beta-meldingsutveksling.difi.no"));
        url.append("/service/local/artifact/maven/content?");
        ConcurrentHashMap<String, String> query = new ConcurrentHashMap<>();
        query.put("r", properties.getProperty("repository", "staging"));
        query.put("g", properties.getProperty("groupId", "no.difi.meldingsutveksling"));
        query.put("a", properties.getProperty("artifactId", "integrasjonspunkt"));
        query.put("v", version);
        if (classifier != null) {
            query.put("e", classifier);
        }
        String reduce = query.reduce(1, (k, v) -> k + "=" + v, (r1, r2) -> r1 + "&" + r2);
        url.append(reduce);
        return new URL(url.toString());
    }

}
