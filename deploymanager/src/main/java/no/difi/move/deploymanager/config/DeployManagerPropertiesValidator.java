package no.difi.move.deploymanager.config;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class DeployManagerPropertiesValidator implements Validator {

    public static final String NOT_EMPTY_MESSAGE = "Property must not be empty.";

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == DeployManagerProperties.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        validateNonEmpty(errors, "root");
        validateNonEmpty(errors, "nexus");
        validateNonEmpty(errors, "repository");
        validateNonEmpty(errors, "groupId");
        validateNonEmpty(errors, "artifactId");
        validateNonEmpty(errors, "shutdownURL");
        validateNonEmpty(errors, "healthURL");
        validateNonEmpty(errors, "nexusProxyURL");

        DeployManagerProperties properties = (DeployManagerProperties) o;
        DeployManagerProperties.IntegrasjonspunktProperties integrasjonspunktProps = properties.getIntegrasjonspunkt();
        if (null == integrasjonspunktProps) {
            errors.reject("integrasjonspunkt", "integrasjonspunkt configuration missing.");
        } else {
            errors.pushNestedPath("integrasjonspunkt");
            ValidationUtils.invokeValidator(new IntegrasjonspunktPropertiesValidator(), integrasjonspunktProps, errors);
            errors.popNestedPath();
        }
    }

    private void validateNonEmpty(Errors errors, String name) {
        ValidationUtils.rejectIfEmpty(errors, name, String.format("%s.empty", name), null, NOT_EMPTY_MESSAGE);
    }

}
