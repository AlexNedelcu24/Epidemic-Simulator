package org.example;

import mpi.MPI;
import org.example.domain.*;
import org.example.repository.AreaRepository;
import org.example.repository.PageRepository;
import org.example.repository.SimulationRepository;
import org.example.repository.UserRepository;
import org.example.service.AreaService;
import org.example.service.PopulationService;
import org.example.service.SimulationService;
import org.example.validators.SimulationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.xml.bind.ValidationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    PageRepository pageRepository;

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        /*
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();

        if (me == 0) {

        } else {
            SpringApplication application = new SpringApplication(Main.class);
            application.setWebEnvironment(false);
            application.run(args);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MPI.Finalize();
        */

        /*SimulationValidator simulationValidator =new SimulationValidator();
        SimulationRepository simulationRepository1 = new SimulationRepository();
        UserRepository userRepository1 = new UserRepository();
        SimulationService simulationService = new SimulationService(simulationRepository1, userRepository1, simulationValidator);

        simulationService.runMpi();*/



    }

    public void run(String... args) throws Exception {
        System.out.println(userRepository.getAllUsers());

        /*int populationSize = 3000000;
        int initiallyInfected = 20;
        int minContacts = 7;
        int maxContacts = 10;
        int minIncubationPeriod = 2;
        int maxIncubationPeriod = 7;
        int minInfectiousPeriod = 3;
        int maxInfectiousPeriod = 15;
        String simulationName = "3";


        SimulationValidator simulationValidator =new SimulationValidator();

        PopulationService populationService = new PopulationService(areaRepository);
        SimulationService simulationService = new SimulationService(simulationRepository, userRepository, simulationValidator);


        Population population = new Population(populationSize,
                initiallyInfected,
                minContacts,
                maxContacts,
                minIncubationPeriod,
                maxIncubationPeriod,
                minInfectiousPeriod,
                maxInfectiousPeriod );



        List<Human> humans = populationService.initializePopulation(
                population,
                simulationName
        );

       /* try{
            simulationService.startSimulation(simulationService.createSimulation((long)2, "sim", 65), humans);
        }catch (ValidationException e){
            System.out.println(e);
        }

        List<Integer> pop = populationService.getPopulationByCountry(humans);
        for (int i = 0; i < pop.size(); i++) {
            Integer p = pop.get(i);
            System.out.println("Țara " + (i + 1) + " are populația: " + p);
        }*/


    }

}