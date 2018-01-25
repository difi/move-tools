package no.difi.move.deploymanager.config;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class DeployManagerPropertiesValidator implements Validator {

    public static final String NOT_EMPTY_MESSAGE = "Property must not be empty.";
    private final long minimumSchedulerRateInMs = 120000;

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
        validateSchedulingRate(errors, properties);
        DeployManagerProperties.IntegrasjonspunktProperties integrasjonspunktProps = properties.getIntegrasjonspunkt();
        if (null == integrasjonspunktProps) {
            errors.reject("integrasjonspunkt", "integrasjonspunkt configuration missing.");
        } else {
            errors.pushNestedPath("integrasjonspunkt");
            ValidationUtils.invokeValidator(new IntegrasjonspunktPropertiesValidator(), integrasjonspunktProps, errors);
            errors.popNestedPath();
        }
    }

    private void validateSchedulingRate(Errors errors, DeployManagerProperties properties) {
        String fieldName = "schedulerFixedRateInMs";
        validateNonEmpty(errors, fieldName);

        try {
            long rate = Long.parseLong(properties.getSchedulerFixedRateInMs());
            if (rate < minimumSchedulerRateInMs) {
                errors.rejectValue(
                        fieldName,
                        "inadequate value",
                        null,
                        String.format("Please specify a scheduler rate greater than or equal to %s.", minimumSchedulerRateInMs));
            }
        } catch (NumberFormatException e) {
            errors.rejectValue(fieldName, "invalid value", null, "Invalid scheduling rate.");
        }

    }

    private void validateNonEmpty(Errors errors, String name) {
        ValidationUtils.rejectIfEmpty(errors, name, String.format("%s.empty", name), null, NOT_EMPTY_MESSAGE);
    }

}
