package no.difi.move.deploymanager.domain.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import no.difi.move.deploymanager.DeployManagerMain;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class NexusRepo {

    private final DeployManagerMain manager;

    public NexusRepo(DeployManagerMain manager) {
        this.manager = manager;
    }

    public URL getArtifact(String version, String classifier)
            throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        url.append(manager.getProperties().getProperty("nexus", "https://beta-meldingsutveksling.difi.no"));
        url.append("/service/local/artifact/maven/content?");
        ConcurrentHashMap<String, String> query = new ConcurrentHashMap<>();
        query.put("r", manager.getProperties().getProperty("repository", "staging"));
        query.put("g", manager.getProperties().getProperty("groupId", "no.difi.meldingsutveksling"));
        query.put("a", manager.getProperties().getProperty("artifactId", "integrasjonspunkt"));
        query.put("v", version);
        if (classifier != null) {
            query.put("e", classifier);
        }
        String reduce = query.reduce(1, (k, v) -> k + "=" + v, (r1, r2) -> r1 + "&" + r2);
        url.append(reduce);
        return new URL(url.toString());
    }

}
