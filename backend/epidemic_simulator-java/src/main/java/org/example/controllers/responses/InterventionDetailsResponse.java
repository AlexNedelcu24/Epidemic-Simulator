package org.example.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterventionDetailsResponse {

    private String countryName;
    private String name;
    private int value;

}
