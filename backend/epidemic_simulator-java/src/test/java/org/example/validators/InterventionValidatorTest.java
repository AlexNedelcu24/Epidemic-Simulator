package org.example.validators;

import org.example.domain.Intervention;
import org.example.validators.InterventionValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterventionValidatorTest {

    private final InterventionValidator interventionValidator = new InterventionValidator();

    @Test
    void validateIntervention_ValidCountryId_DoesNotThrowException() {
        Intervention intervention = new Intervention(100, "CountryName", "InterventionName", 10, 1L);
        assertDoesNotThrow(() -> interventionValidator.validateIntervention(intervention));
    }

    @Test
    void validateIntervention_InvalidCountryId_ThrowsException() {
        Intervention intervention = new Intervention(300, "CountryName", "InterventionName", 10, 1L);
        Exception exception = assertThrows(Exception.class, () -> interventionValidator.validateIntervention(intervention));
        assertEquals("CountryId is not valid", exception.getMessage());
    }
}
