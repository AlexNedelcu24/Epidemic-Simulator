package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.*;
import org.example.repository.ResultRepository;
import org.example.repository.SimulationRepository;
import org.example.repository.UserRepository;
import org.example.repository.interfaces.ISimulationRepository;
import org.example.repository.interfaces.IUserRepository;
import org.example.service.interfaces.IResultsService;
import org.example.service.interfaces.ISimulationService;
import org.example.validators.SimulationValidator;

import org.springframework.scheduling.annotation.Async;


import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import mpi.*;

@org.springframework.stereotype.Service(value = "simulationService")
@AllArgsConstructor
public class SimulationService implements ISimulationService {

    private final ISimulationRepository simulationRepository;
    private final IUserRepository userRepository;
    private final IResultsService resultService;
    private final SimulationValidator simulationValidator;
    private final Object monitor = new Object();
    private final Map<Long, List<Integer>> infectionsMap = new HashMap<>();

    private final Map<Long, Map<Integer, Integer>> distanceMap = new HashMap<>();

    private final Map<Long, Map<Integer, Integer>> vaccinMap = new HashMap<>();


    public Simulation saveSimulation(Long userId, String name, int transmissionProbability) throws ValidationException {
        Simulation simulation = createSimulation(userId, name, transmissionProbability);
        return simulationRepository.saveSimulation(simulation);
    }

    public Simulation createSimulation(Long userId, String name, int transmissionProbability) throws ValidationException {
        try {
            Simulation simulation = new Simulation(userId, name, transmissionProbability);
            simulationValidator.validateSimulation(simulation);
            if (userRepository.getUserById(userId) == null) {
                throw new ValidationException("There is no user for this simulation!");
            }
            if (simulationRepository.getSimulationByName(name) != null) {
                throw new ValidationException("Name is already taken!");
            }
            return simulation;
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public List<Simulation> getUsersSimulations(Long userId){
        return simulationRepository.getSimulationsByUser(userId);
    }

    @Async
    public void startSimulation(Simulation simulation, List<Human> humans) throws ValidationException {
        int currentDay = 1;
        boolean hasInfectious;

        int infectiousCount = 0;

        Random random = new Random();

        Long userId = simulation.getUserId();

        distanceMap.remove(userId);
        vaccinMap.remove(userId);

        do {
            hasInfectious = false;



            // Update the health state of each person
            for (Human human : humans) {
                HealthState healthState = human.getHealthState();

                if (human.getState() == State.EXPOSED &&
                        healthState.getExposureDay() + human.getIncubationPeriod() == currentDay) {
                    human.setState(State.INFECTIOUS);

                    healthState.setInfectionDay(currentDay);
                }

                if (human.getState() == State.INFECTIOUS) {

                    if (healthState.getInfectionDay() + human.getInfectiousPeriod() == currentDay) {
                        human.setState(State.REMOVED);
                        healthState.setRecoveryDay(currentDay);
                    }
                }
            }



            // Simulate disease spread
            for (Human human : humans) {
                if (human.getState() == State.INFECTIOUS) {

                    hasInfectious = true;
                    infectiousCount++;




                    Map<Integer, Integer> contactList = human.getContactList();
                    List<Map.Entry<Integer, Integer>> contactsToProcess = new ArrayList<>(contactList.entrySet());

                    synchronized (distanceMap) {
                        if (distanceMap.containsKey(userId)) {
                            Map<Integer, Integer> userData = distanceMap.get(userId);
                            if (userData.containsKey(human.getCountry())) {

                                contactsToProcess = contactsToProcess.subList(0, contactsToProcess.size() / 20);
                            }
                        }

                    }


                    for (Map.Entry<Integer, Integer> contact : contactsToProcess) {
                        Human contactHuman = humans.get(contact.getKey() - 1); // Assuming IDs are 1-based
                        if (contactHuman.getState() == State.SUSCEPTIBLE) {
                            int contactPeriod = contact.getValue();
                            //double p = (simulation.getTransmissionProbability() / 100.0);
                            double p = 1 - Math.pow(1 - (simulation.getTransmissionProbability() / 1000.0), contactPeriod);


                            // Check vaccination map

                            synchronized (vaccinMap) {
                                if (vaccinMap.containsKey(userId)) {
                                    Map<Integer, Integer> userVaccinationData = vaccinMap.get(userId);
                                    if (userVaccinationData.containsKey(human.getCountry())) {
                                        int vaccinationPercent = userVaccinationData.get(human.getCountry());

                                        p -= vaccinationPercent / 100.0;
                                    }

                                    if (userVaccinationData.containsKey(contactHuman.getCountry())) {
                                        int contactVaccinationPercent = userVaccinationData.get(contactHuman.getCountry());

                                        p -= contactVaccinationPercent / 100.0;
                                    }

                                }

                                if (p < 0) p = 0;
                            }

                            if (random.nextDouble() < p) {
                                contactHuman.setState(State.EXPOSED);
                                contactHuman.getHealthState().setExposureDay(currentDay);
                            }

                        }
                    }

                }
            }

            System.out.println("Day " + currentDay + ": " + infectiousCount + " infectious individuals");
            resultService.saveResult(currentDay, infectiousCount, simulation.getId());
            infectiousCount = 0;

            currentDay++;

            User currentUser = userRepository.getUserById(userId);


            // Decrement days in distanceMap for the current user
            synchronized (distanceMap) {
                if (distanceMap.containsKey(userId)) {
                    Map<Integer, Integer> userDistanceData = distanceMap.get(userId);
                    List<Integer> keysToRemove = new ArrayList<>();

                    for (Map.Entry<Integer, Integer> entry : userDistanceData.entrySet()) {
                        int newDays = entry.getValue() - 1;
                        if (newDays <= 0) {
                            keysToRemove.add(entry.getKey());
                        } else {
                            entry.setValue(newDays);
                        }
                    }

                    for (Integer key : keysToRemove) {
                        userDistanceData.remove(key);
                    }
                }
            }



            // Update infections map if user infections number is true
            if (currentUser.getInfectionsNumber()) {
                synchronized (infectionsMap) {

                    var countriesInfectiousCount = getIntegers(humans);

                    infectionsMap.put(userId, countriesInfectiousCount);

                    //User user = currentUser;

                }

                User oldUser = userRepository.getUserById(userId);
                currentUser.setInfectionsNumber(false);
                System.out.println(" ");

                userRepository.updateUser(currentUser, oldUser);
            }

            System.out.println(" ");

            synchronized (monitor) {
                while (userRepository.getUserById(userId).getHasPaused()) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }


            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }




        } while (hasInfectious);


        var countriesInfectiousCount = getIntegers(humans);
        infectionsMap.put(userId, countriesInfectiousCount);

    }

    private static List<Integer> getIntegers(List<Human> humans) {
        List<Integer> countriesInfectiousCount = new ArrayList<>(255);
        for (int i = 0; i < 255; i++) {
            countriesInfectiousCount.add(0);
        }

        for (Human human : humans) {
            if (human.getState() == State.INFECTIOUS) {
                int countryIndex = human.getCountry() - 1;
                countriesInfectiousCount.set(countryIndex, countriesInfectiousCount.get(countryIndex) + 1);
            }
        }
        return countriesInfectiousCount;
    }


    public void pauseSimulation(Long id) {
        synchronized (monitor) {
            User user = userRepository.getUserById(id);
            user.setHasPaused(true);
            userRepository.updateUser(user, userRepository.getUserById(id));
        }
    }

    public void continueSimulation(Long id) {
        synchronized (monitor) {
            User user = userRepository.getUserById(id);
            user.setHasPaused(false);
            userRepository.updateUser(user, userRepository.getUserById(id));
            monitor.notifyAll();
        }
    }

    /*public List<Integer> getInfectionsNumberPerCountry(Long id){
        synchronized (infectionsMap) {
            return infectionsMap.get(id);
        }
    }*/

    public List<Double> getInfectionsNumberPerCountry(Long id, List<Integer> populationList) {
        synchronized (infectionsMap) {
            List<Integer> infections = infectionsMap.get(id);
            /*if (infections == null || populationList.size() != 255) {
                throw new IllegalArgumentException("Invalid data provided");
            }*/

            List<Double> infectionPercentages = new ArrayList<>(255);
            for (int i = 0; i < 255; i++) {
                int population = populationList.get(i);
                int infectionCount = infections.get(i);
                double percentage = population > 0 ? (infectionCount / (double) population) * 100 : 0;

                BigDecimal roundedPercentage = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
                infectionPercentages.add(roundedPercentage.doubleValue());
            }

            return infectionPercentages;
        }
    }

    public void setInfections(Long id) {
        User user = userRepository.getUserById(id);
        user.setInfectionsNumber(true);
        userRepository.updateUser(user, userRepository.getUserById(id));
    }

    public void addToDistanceMap(Long userId, Integer countryId, Integer days) {
        synchronized (distanceMap) {
            distanceMap.computeIfAbsent(userId, k -> new HashMap<>()).merge(countryId, days, Integer::sum);
        }
    }

    public void addToVaccinMap(Long userId, Integer countryId, Integer percent) {
        synchronized (vaccinMap) {
            vaccinMap.computeIfAbsent(userId, k -> new HashMap<>()).merge(countryId, percent, (oldValue, newValue) -> newValue);
        }
    }


    /*public void runMpi() {
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("Hi from <" + me + ">" + " and the size is " + size);
    }*/


    @Async
    public void startParallel(Simulation simulation, List<Human> humans) {
        int currentDay = 1;
        AtomicBoolean hasInfectious = new AtomicBoolean(false);
        AtomicInteger infectiousCount = new AtomicInteger();
        Random random = new Random();
        Long userId = simulation.getUserId();

        int nrThreads = 12;

        // Create a thread pool with 6 threads
        ExecutorService executor = Executors.newFixedThreadPool(nrThreads);

        try {
            do {
                hasInfectious.set(false);
                List<Future<Void>> futures = new ArrayList<>();

                for (int i = 0; i < nrThreads; i++) {
                    int fromIndex = i;

                    // Submit tasks to the executor service
                    int finalCurrentDay = currentDay;
                    futures.add(executor.submit(() -> {
                        for (int j = fromIndex; j < humans.size(); j += nrThreads) {
                            Human human = humans.get(j);
                            synchronized (human) {
                                HealthState healthState = human.getHealthState();

                                if (human.getState() == State.EXPOSED &&
                                        healthState.getExposureDay() + human.getIncubationPeriod() == finalCurrentDay) {
                                    human.setState(State.INFECTIOUS);
                                    healthState.setInfectionDay(finalCurrentDay);
                                }

                                if (human.getState() == State.INFECTIOUS) {
                                    if (healthState.getInfectionDay() + human.getInfectiousPeriod() == finalCurrentDay) {
                                        human.setState(State.REMOVED);
                                        healthState.setRecoveryDay(finalCurrentDay);
                                    }
                                }
                            }
                        }

                        for (int j = fromIndex; j < humans.size(); j += nrThreads) {
                            Human human = humans.get(j);

                            if (human.getState() == State.INFECTIOUS) {
                                hasInfectious.set(true);
                                infectiousCount.getAndIncrement();

                                Map<Integer, Integer> contactList = human.getContactList();
                                for (Map.Entry<Integer, Integer> contact : contactList.entrySet()) {
                                    Human contactHuman = humans.get(contact.getKey() - 1); // Assuming IDs are 1-based
                                    synchronized (contactHuman) {
                                        if (contactHuman.getState() == State.SUSCEPTIBLE) {
                                            int contactPeriod = contact.getValue();
                                            double p = 1 - Math.pow(1 - (simulation.getTransmissionProbability() / 1000.0), contactPeriod);
                                            //double p = (simulation.getTransmissionProbability() / 100.0);
                                            if (random.nextDouble() < p) {
                                                contactHuman.setState(State.EXPOSED);
                                                contactHuman.getHealthState().setExposureDay(finalCurrentDay);
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        return null;
                    }));

                }

                for (Future<Void> future : futures) {
                    future.get(); // Wait for all tasks to complete
                }

                System.out.println("Day " + currentDay + ": " + infectiousCount + " infectious individuals");
                resultService.saveResult(currentDay, infectiousCount.get(), simulation.getId());
                infectiousCount.set(0);
                currentDay++;

                User currentUser = userRepository.getUserById(userId);

            } while (hasInfectious.get());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

        } catch (ValidationException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }



    public Simulation getSimulationById(Long id){
        return simulationRepository.getSimulationById(id);
    }
}
