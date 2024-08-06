package org.example.validators;

import org.example.domain.Population;
import org.example.validators.PopulationValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PopulationValidatorTest {

    private final PopulationValidator populationValidator = new PopulationValidator();

    @Test
    void validatePopulation_ValidPopulation_DoesNotThrowException() {
        Population population = new Population(1000, 10, 5, 15, 1, 5, 2, 7, 1L);
        assertDoesNotThrow(() -> populationValidator.validatePopulation(population));
    }

    @Test
    void validatePopulation_InvalidPopulationSize_ThrowsException() {
        Population population = new Population(5000000, 10, 5, 15, 1, 5, 2, 7, 1L);
        Exception exception = assertThrows(Exception.class, () -> populationValidator.validatePopulation(population));
        assertEquals("PopulationSize is not valid", exception.getMessage());
    }

    @Test
    void validatePopulation_InvalidInitiallyInfected_ThrowsException() {
        Population population = new Population(1000, 1500, 5, 15, 1, 5, 2, 7, 1L);
        Exception exception = assertThrows(Exception.class, () -> populationValidator.validatePopulation(population));
        assertEquals("InitiallyInfected is not valid", exception.getMessage());
    }
}
