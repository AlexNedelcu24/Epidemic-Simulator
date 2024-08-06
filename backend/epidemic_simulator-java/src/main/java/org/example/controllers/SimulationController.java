package org.example.controllers;

import org.example.controllers.requests.InterventionRequest;
import org.example.controllers.requests.SimulationDetailsRequest;
import org.example.controllers.requests.UserRequest;
import org.example.controllers.requests.SimulationRequest;
import org.example.domain.Human;
import org.example.domain.Intervention;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.service.InterventionService;
import org.example.service.PopulationService;
import org.example.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/sim")
//@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SimulationController {

    private PopulationService populationService;
    private SimulationService simulationService;
    private InterventionService interventionService;
    private Map<Long, List<Human>> userHumans;
    private Map<Long, Simulation> userSimulations;

    private Map<Long, Boolean> simulationStatus;

    public SimulationController(PopulationService populationService, SimulationService simulationService, InterventionService interventionService) {
        this.populationService = populationService;
        this.simulationService = simulationService;
        this.interventionService = interventionService;
        this.userHumans = new HashMap<>();
        this.userSimulations = new HashMap<>();
        this.simulationStatus = new HashMap<>();
    }


    @RequestMapping(value = "/createPopulation", method = RequestMethod.POST)
    public ResponseEntity<String> createPopulation(@RequestBody SimulationRequest simulationRequest){
        try{

            userHumans.remove(simulationRequest.getUserId());

            Simulation simulation = simulationService.saveSimulation(simulationRequest.getUserId(), simulationRequest.getName(), simulationRequest.getTransmissionProbability()) ;
            //Simulation simulation = simulationService.createSimulation(simulationRequest.getUserId(), simulationRequest.getName(), simulationRequest.getTransmissionProbability()) ;


            /*Population population = new Population(simulationRequest.getPopulationSize(),
                    simulationRequest.getInitiallyInfected(),
                    simulationRequest.getMinContacts(),
                    simulationRequest.getMaxContacts(),
                    simulationRequest.getMinIncubationPeriod(),
                    simulationRequest.getMaxIncubationPeriod(),
                    simulationRequest.getMinInfectiousPeriod(),
                    simulationRequest.getMaxInfectiousPeriod(),
                    simulation.getId());*/

            Population population = populationService.savePopulation(simulationRequest.getPopulationSize(),
                    simulationRequest.getInitiallyInfected(),
                    simulationRequest.getMinContacts(),
                    simulationRequest.getMaxContacts(),
                    simulationRequest.getMinIncubationPeriod(),
                    simulationRequest.getMaxIncubationPeriod(),
                    simulationRequest.getMinInfectiousPeriod(),
                    simulationRequest.getMaxInfectiousPeriod(),
                    simulation.getId());

            userSimulations.put(simulationRequest.getUserId(), simulation);


            List<Human> humans = populationService.initializePopulation(
                    population,
                    simulationRequest.getName()
            );
            userHumans.put(simulationRequest.getUserId(), humans);

            simulationStatus.put(simulation.getUserId(), true);


            return ResponseEntity.ok("ok!");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/startSimulation", method = RequestMethod.POST)
    public ResponseEntity<String> startSimulation(@RequestBody UserRequest userRequest) {

        Long userId = userRequest.getId();
        Simulation simulation = userSimulations.get(userId);
        List<Human> humans = userHumans.get(userId);

        simulationService.continueSimulation(userRequest.getId());

        if (simulation == null || humans == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No simulation or population found for user id: " + userId);
        }

       /* List<Human> humansCopy = new ArrayList<>();
        for (Human human : humans) {
            Human newHuman = new Human(
                    human.getValue(),
                    human.getSimulationName(),
                    human.getCountry(),
                    human.getIncubationPeriod(),
                    human.getInfectiousPeriod(),
                    new HashMap<>(human.getContactList()),
                    human.getState()
            );
            humansCopy.add(newHuman);
        }*/


        ExecutorService taskExecutor = Executors.newFixedThreadPool(1);

        taskExecutor.submit(() -> {

            try {
                simulationService.startSimulation(simulation, humans);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            simulationStatus.put(simulation.getUserId(), false);
            /*userSimulations.remove(userId);
            userHumans.remove(userId);*/
        });

        return ResponseEntity.ok("Simulation start!");
    }

    @PostMapping("/pauseSimulation")
    public ResponseEntity<String> pauseSimulation(@RequestBody UserRequest userRequest) {
        simulationService.pauseSimulation(userRequest.getId());
        return ResponseEntity.ok("Simulation paused.");
    }

    @PostMapping("/continueSimulation")
    public ResponseEntity<String> continueSimulation(@RequestBody UserRequest userRequest) {
        simulationService.continueSimulation(userRequest.getId());
        return ResponseEntity.ok("Simulation continued.");
    }

    @RequestMapping(value = "/infectionsPerCountry", method = RequestMethod.POST)
    public ResponseEntity<?> getInfectionsPerCountry(@RequestBody UserRequest userRequest) {

        Long userId = userRequest.getId();
        boolean isSimulationRunning = simulationStatus.get(userId);

        if ( !isSimulationRunning) {
            return ResponseEntity.ok("Simulation is over");
        }

        try {
            List<Human> humans = userHumans.get(userRequest.getId());
            List<Integer> populationByCountry = populationService.getPopulationByCountry(humans);
            simulationService.setInfections(userRequest.getId());
            List<Double> infections = simulationService.getInfectionsNumberPerCountry(userRequest.getId(), populationByCountry);
            return ResponseEntity.ok(infections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/applyIntervention", method = RequestMethod.POST)
    public ResponseEntity<String> applyIntervenyion(@RequestBody InterventionRequest interventionRequest) {

        Long userId = interventionRequest.getUserId();
        boolean isSimulationRunning = simulationStatus.get(userId);
        if ( !isSimulationRunning) {
            return ResponseEntity.ok("Simulation is over");
        }

        try{
            Long simulationId = userSimulations.get(userId).getId();

            if(Objects.equals(interventionRequest.getName(), "Vaccination")) {
                Intervention intervention = interventionService.saveIntervention(interventionRequest.getCountry(), interventionRequest.getCountryName(), "Vaccination", interventionRequest.getValue(), simulationId);

                simulationService.addToDistanceMap(interventionRequest.getUserId(), interventionRequest.getCountry(), interventionRequest.getValue());
            }
            if(Objects.equals(interventionRequest.getName(), "Social Distancing")) {
                Intervention intervention = interventionService.saveIntervention(interventionRequest.getCountry(), interventionRequest.getCountryName(), "Social Distancing", interventionRequest.getValue(), simulationId);

                simulationService.addToVaccinMap(interventionRequest.getUserId(), interventionRequest.getCountry(), interventionRequest.getValue());
            }
            return ResponseEntity.ok("Intervention applied!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    /*@GetMapping("/rp")
    public String runParallelMethod() {
        simulationService.runMpi();
        return "Metoda paralelizată a fost executată.";
    }*/



    @RequestMapping(value = "/startParallel", method = RequestMethod.POST)
    public ResponseEntity<String> startParallel(@RequestBody UserRequest userRequest) {

        Long userId = userRequest.getId();
        Simulation simulation = userSimulations.get(userId);
        List<Human> humans = userHumans.get(userId);

        simulationService.continueSimulation(userRequest.getId());

        if (simulation == null || humans == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No simulation or population found for user id: " + userId);
        }


        ExecutorService taskExecutor = Executors.newFixedThreadPool(1);

        taskExecutor.submit(() -> {

            simulationService.startParallel(simulation, humans);

            simulationStatus.put(simulation.getUserId(), false);

        });

        return ResponseEntity.ok("Simulation start!");
    }








    @RequestMapping(value = "/usersSimulations", method = RequestMethod.POST)
    public ResponseEntity<?> getUsersSimulations(@RequestBody UserRequest userRequest) {

        Long userId = userRequest.getId();

        try {
            List<Simulation> simulations = simulationService.getUsersSimulations(userId);
            return ResponseEntity.ok(simulations);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }







    @RequestMapping(value = "/simulatingOver", method = RequestMethod.POST)
    public ResponseEntity<String> getSimulationStatus(@RequestBody UserRequest userRequest) {

        Long userId = userRequest.getId();
        boolean isSimulationRunning = simulationStatus.get(userId);

        try {
            if ( !isSimulationRunning) {
                return ResponseEntity.ok("Simulation is over");
            } else {
                return ResponseEntity.ok("Simulation is running");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




    @RequestMapping(value = "/simulationDetails", method = RequestMethod.POST)
    public ResponseEntity<?> getSimulationDetails(@RequestBody UserRequest userRequest) {

        Long simulationId = userRequest.getId();

        try {
            Simulation simulation = simulationService.getSimulationById(simulationId);
            Population population = populationService.getPopulationBySimulation(simulationId);

            SimulationDetailsRequest s = new SimulationDetailsRequest(simulation.getName(), simulation.getTransmissionProbability(), population.getPopulationSize(), population.getInitiallyInfected(), population.getMinContacts(), population.getMaxContacts(), population.getMinIncubationPeriod(), population.getMaxIncubationPeriod(), population.getMinInfectiousPeriod(), population.getMaxInfectiousPeriod());

            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
