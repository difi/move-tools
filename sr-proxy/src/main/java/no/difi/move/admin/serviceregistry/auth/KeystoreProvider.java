package no.difi.move.admin.serviceregistry.auth;

import no.difi.move.admin.serviceregistry.config.KeyStoreProperties;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeystoreProvider {

    private final KeyStore keystore;

    private KeystoreProvider(KeyStore keyStore) {
        this.keystore = keyStore;
    }

    public static KeystoreProvider from(KeyStoreProperties properties) {
        final KeyStore keyStore = loadKeyStore(properties);
        return new KeystoreProvider(keyStore);
    }

    static KeyStore loadKeyStore(KeyStoreProperties properties) {
        String type = properties.getType();
        String password = properties.getPassword();
        Resource path = properties.getPath();
        try {
            KeyStore keyStore = KeyStore.getInstance(type);
            if (path == null || "none".equalsIgnoreCase(path.getFilename())) {
                keyStore.load(null, password.toCharArray());
            } else {
                keyStore.load(path.getInputStream(), password.toCharArray());
            }
            return keyStore;

        } catch (KeyStoreException e) {
            throw new IllegalStateException("Unable to load KeyStore", e);

        } catch (IOException e) {
            throw new IllegalStateException("Could not open keystore file", e);

        } catch (CertificateException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to load keystore file", e);
        }
    }

    public KeyStore getKeyStore() {
        return keystore;
    }
}
