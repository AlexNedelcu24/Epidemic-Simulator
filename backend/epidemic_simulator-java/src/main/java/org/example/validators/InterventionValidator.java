package org.example.validators;

import lombok.AllArgsConstructor;
import org.example.domain.Intervention;
import org.example.domain.Population;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class InterventionValidator {

    private boolean isValidCountryId(Integer countryId) {
        return ( countryId >= 1 && countryId <= 255);
    }

    public void validateIntervention(Intervention intervention) throws Exception {
        if(!isValidCountryId(intervention.getCountryId())) {
            throw new Exception("CountryId is not valid");
        }
    }

}
