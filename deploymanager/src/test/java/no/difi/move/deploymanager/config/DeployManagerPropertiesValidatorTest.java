package no.difi.move.deploymanager.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.net.URL;

public class DeployManagerPropertiesValidatorTest {

    private DeployManagerPropertiesValidator validatorUnderTest = new DeployManagerPropertiesValidator();

    private DeployManagerProperties propertiesMock;

    @Before
    public void setUp() throws IOException {
        propertiesMock = new DeployManagerProperties();
        propertiesMock.setNexus(new URL("http://some.url.here"));
        propertiesMock.setArtifactId("integrasjonspunkt");
        propertiesMock.setGroupId("no.difi.meldingsutveksling");
        propertiesMock.setHealthURL(new URL("http://health.url.here"));
        propertiesMock.setNexusProxyURL(new URL("http://nexus.proxy.url"));
        propertiesMock.setRepository("itest");
        propertiesMock.setRoot("folder/path");
        propertiesMock.setShutdownURL(new URL("http://shutdown.url.here"));
        propertiesMock.setVerbose(false);
        DeployManagerProperties.IntegrasjonspunktProperties intPropMock = new DeployManagerProperties.IntegrasjonspunktProperties();
        intPropMock.setProfile("itest");
        propertiesMock.setIntegrasjonspunkt(intPropMock);
    }

    @Test
    public void nonEmptyConfigurationValues_validationShouldSucceed() {
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "allPropertiesNonEmpty");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void missingNexusURL_validationShouldFail() {
        propertiesMock.setNexus(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingNexusURL");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("nexus"));
    }

    @Test
    public void missingRoot_validationShouldFail() {
        propertiesMock.setRoot(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingRoot");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("root"));
    }

    @Test
    public void missingRepository_validationShouldFail() {
        propertiesMock.setRepository(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingRepository");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("repository"));
    }

    @Test
    public void missingGroupId_validationShouldFail() {
        propertiesMock.setGroupId(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingGroupId");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("groupId"));
    }

    @Test
    public void missingArtifactId_validationShouldFail() {
        propertiesMock.setArtifactId(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingArtifactId");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("artifactId"));
    }

    @Test
    public void missingShutdownURL_validationShouldFail() {
        propertiesMock.setShutdownURL(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingShutdownURL");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("shutdownURL"));
    }

    @Test
    public void missingHealthURL_validationShouldFail() {
        propertiesMock.setHealthURL(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingHealthURL");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("healthURL"));
    }

    @Test
    public void missingNexusProxyURL_validationShouldFail() {
        propertiesMock.setNexusProxyURL(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingNexusProxyURL");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("nexusProxyURL"));
    }

    @Test
    public void missingIntegrasjonspunkt_validationShouldFail() {
        propertiesMock.setIntegrasjonspunkt(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingIntegrasjonspunkt");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void missingIntegrasjonspunktProfile_validationShouldFail() {
        propertiesMock.getIntegrasjonspunkt().setProfile(null);
        Errors errors = new BeanPropertyBindingResult(propertiesMock, "missingIntegrasjonspunktProfile");
        validatorUnderTest.validate(propertiesMock, errors);
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("integrasjonspunkt.profile"));
    }

}
