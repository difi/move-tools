package no.difi.move.deploymanager.repo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import no.difi.move.deploymanager.DeployManagerMain;
import no.difi.move.deploymanager.config.DeployManagerProperties;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class DeployDirectoryRepo {

    private final DeployManagerProperties properties;

    public DeployDirectoryRepo(DeployManagerProperties properties) {
        this.properties = properties;
    }

    public Properties getMetadata() throws IOException {
        File propertiesFile = getFile("meta.properties");

        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile)) {
            properties.load(is);
        }

        return properties;
    }

    public void setMetadata(Properties properties) throws IOException {
        File propertiesFile = getFile("meta.properties");

        try (FileOutputStream os = new FileOutputStream(propertiesFile)) {
            properties.store(os, "Automatically generated");
        }
    }

    private File getFile(String file) throws IOException {
        File root = getRoot();
        File propertiesFile = new File(root, file);
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile();
        }
        return propertiesFile;
    }

    private File getRoot() {
        File root = new File(properties.getRoot());
        if (!root.exists()) {
            root.mkdir();
        }
        return root;
    }

}
