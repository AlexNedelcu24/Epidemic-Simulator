package org.example.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterventionRequest {

    private Long userId;
    private String name;
    private int country;
    private String countryName;
    private int value;

}
