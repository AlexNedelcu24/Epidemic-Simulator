package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.controllers.requests.InterventionDetailsResponse;
import org.example.controllers.requests.UserRequest;
import org.example.domain.Intervention;
import org.example.service.InterventionService;
import org.example.service.interfaces.IInterventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class InterventionController {

    private IInterventionService interventionService;


    @PostMapping
    public ResponseEntity<?> getInterventions(@RequestBody UserRequest userRequest) {

        Long simulationId = userRequest.getId();

        try {
            List<Intervention> interventions = interventionService.getInterventionBySimulation(simulationId);
            List<InterventionDetailsResponse> response = new ArrayList<>();

            for(Intervention intervention: interventions){
                InterventionDetailsResponse interventionDetailsResponse = new InterventionDetailsResponse(intervention.getCountryName(), intervention.getName(), intervention.getValue());
                response.add(interventionDetailsResponse);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
