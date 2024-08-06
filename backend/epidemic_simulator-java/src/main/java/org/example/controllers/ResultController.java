package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.controllers.responses.ResultsDetailsResponse;
import org.example.controllers.requests.UserRequest;
import org.example.domain.Intervention;
import org.example.domain.Result;
import org.example.service.ResultService;
import org.example.service.interfaces.IResultsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ResultController {

    private IResultsService resultService;


    @PostMapping
    public ResponseEntity<?> getResults(@RequestBody UserRequest userRequest) {

        Long simulationId = userRequest.getId();

        try {
            List<Result> results = resultService.getResultsBySimulation(simulationId);
            List<ResultsDetailsResponse> l = new ArrayList<>();

            for (Result result : results) {
                ResultsDetailsResponse r = new ResultsDetailsResponse(result.getDay(), result.getInfectionsNumber());
                l.add(r);
            }

            return ResponseEntity.ok(l);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
