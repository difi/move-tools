package no.difi.serviceregistry.proxy.auth;

import no.difi.serviceregistry.proxy.config.KeyStoreProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KeyStore.class, KeystoreAccessor.class, KeystoreProvider.class})
public class KeystoreAccessorTest_PowerMock {

    @Mock
    KeyStore keyStore;

    @Mock
    Resource file;

    @Before
    public void before()throws Exception{
        PowerMockito.mockStatic(KeyStore.class);
        when(KeyStore.getInstance(anyString())).thenReturn(keyStore);

        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
    }

    @Test
    public void testLoadKeyStoreWithoutPath()throws Exception{

        KeyStoreProperties properties = new KeyStoreProperties();
        properties.setAlias("alias");
        properties.setPassword("password");
        properties.setType("type");

        KeystoreAccessor keystoreAccessor = new KeystoreAccessor(properties);

        Assert.assertEquals(keyStore, keystoreAccessor.getKeyStore());
    }

    @Test
    public void testLoadKeyStoreWithNONEPath()throws Exception{

        KeyStoreProperties properties = new KeyStoreProperties();
        properties.setAlias("alias");
        properties.setPassword("password");
        properties.setType("type");

        properties.setPath(new PathResource("NONE"));

        KeystoreAccessor keystoreAccessor = new KeystoreAccessor(properties);

        Assert.assertEquals(keyStore, keystoreAccessor.getKeyStore());
    }


    @Test
    public void testLoadKeyStoreWithPath() throws Exception{

        KeyStoreProperties properties = new KeyStoreProperties();
        properties.setAlias("alias");
        properties.setPassword("password");
        properties.setType("type");
        properties.setPath(file);

        KeystoreAccessor keystoreAccessor = new KeystoreAccessor(properties);

        Assert.assertEquals(keyStore, keystoreAccessor.getKeyStore());
    }
}
