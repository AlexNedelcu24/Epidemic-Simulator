package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.*;
import org.example.repository.AreaRepository;
import org.example.repository.PopulationRepository;
import org.example.repository.SimulationRepository;
import org.example.repository.interfaces.IAreaRepository;
import org.example.repository.interfaces.IPopulationRepository;
import org.example.repository.interfaces.ISimulationRepository;
import org.example.service.interfaces.IPopulationService;
import org.example.validators.PopulationValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.ValidationException;
import java.util.*;

@org.springframework.stereotype.Service(value = "populationService")
@AllArgsConstructor
public class PopulationService implements IPopulationService {

    IAreaRepository areaRepository;

    private final PopulationValidator populationValidator;
    private final IPopulationRepository populationRepository;
    private final ISimulationRepository simulationRepository;

    public Population savePopulation(int populationSize, int initiallyInfected, int minContacts, int maxContacts, int minIncubationPeriod, int maxIncubationPeriod, int minInfectiousPeriod, int maxInfectiousPeriod, Long simulationId) throws ValidationException {
        try {
            Population population = new Population(populationSize, initiallyInfected, minContacts, maxContacts, minIncubationPeriod, maxIncubationPeriod, minInfectiousPeriod, maxInfectiousPeriod, simulationId);
            populationValidator.validatePopulation(population);
            if (simulationRepository.getSimulationById(simulationId) == null) {
                throw new ValidationException("There is no simulation for this population!");
            }

            return populationRepository.savePopulation(population);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public List<Human> initializePopulation(Population population, String simulationName) {
        List<Human> humans = new ArrayList<>();
        int populationSize = population.getPopulationSize();
        int initiallyInfected = population.getInitiallyInfected();
        int minContacts = population.getMinContacts();
        int maxContacts = population.getMaxContacts();
        int minIncubationPeriod = population.getMinIncubationPeriod();
        int maxIncubationPeriod = population.getMaxIncubationPeriod();
        int minInfectiousPeriod = population.getMinInfectiousPeriod();
        int maxInfectiousPeriod = population.getMaxInfectiousPeriod();

        Random random = new Random();


        AreaService areaService = new AreaService(areaRepository);
        List<Area> areas = areaService.getAllAreas();
        double totalArea = areas.stream().mapToDouble(Area::getArea).sum();
        Map<Long, Double> countryProbabilities = new HashMap<>();
        for (Area area : areas) {
            countryProbabilities.put(area.getId(), area.getArea() / totalArea);
        }

        Map<Integer, List<Integer>> countryHumanMap = new HashMap<>();
        for (int i = 1; i <= 255; i++) {
            countryHumanMap.put(i, new ArrayList<>());
        }

        for (int i = 1; i <= populationSize; i++) {
            //int country = (i % 255) + 1;  // Ensures an even distribution across 255 countries
            int country = (int) getRandomCountry(countryProbabilities, random);
            int incubationPeriod = random.nextInt(maxIncubationPeriod - minIncubationPeriod + 1) + minIncubationPeriod;
            int infectiousPeriod = random.nextInt(maxInfectiousPeriod - minInfectiousPeriod + 1) + minInfectiousPeriod;

            Map<Integer, Integer> contactList = new HashMap<>();

            State state = State.SUSCEPTIBLE;
            humans.add(new Human(i, simulationName, country, incubationPeriod, infectiousPeriod, contactList, state));

            countryHumanMap.get(country).add(i);
        }

        List<Human> humansWithInternationalContacts = new ArrayList<>();
        List<Human> humansWithNationalContacts = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            if (i < populationSize * 0.05) {
                humansWithInternationalContacts.add(humans.get(i));
            } else {
                humansWithNationalContacts.add(humans.get(i));
            }
        }

        for (Human human : humansWithInternationalContacts) {
            int numberOfContacts = random.nextInt(maxContacts - minContacts + 1) + minContacts;
            Set<Integer> contacts = new HashSet<>();

            int country = human.getCountry();

            List<Integer> sameCountryHumans = countryHumanMap.get(country);

            //Collections.shuffle(sameCountryHumans, random);

            int sameCountryContacts = Math.min((int) (numberOfContacts * 0.9), sameCountryHumans.size() - 1);

            int maxIterations = 50;
            int iterations = 0;

            while (contacts.size() + human.getContactList().size() < sameCountryContacts && iterations < maxIterations) {
                int randomIndex = random.nextInt(sameCountryHumans.size());
                int contact = sameCountryHumans.get(randomIndex);

                if (contact != human.getValue() && !human.getContactList().containsKey(contact)) {
                    contacts.add(contact);
                }
                iterations++;
            }

            /*for (int contact : sameCountryHumans) {
                if (contacts.size() + human.getContactList().size() >= sameCountryContacts) {
                    break;
                }
                if (contact != human.getValue() && !human.getContactList().containsKey(contact)) {
                    contacts.add(contact);
                }
            }*/

            while (contacts.size() + human.getContactList().size() < numberOfContacts ) {

                int contactValue = random.nextInt(populationSize) + 1;

                if (!human.getContactList().containsKey(contactValue) && human.getCountry()!=humans.get(contactValue-1).getCountry()) {
                    contacts.add(contactValue);
                }
            }

            for (int contactValue : contacts) {
                int contactPeriod = random.nextInt(24) + 1;

                human.getContactList().put(contactValue, contactPeriod);
                humans.get(contactValue - 1).getContactList().put(human.getValue(), contactPeriod);
               /* if (human.getCountry() != humans.get(contactValue - 1).getCountry())
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");*/
            }

            if (human.getValue() % 100000 == 0) {
                System.out.println("Human " + human.getValue() + " has " + human.getContactList().size() + " contacts");
            }

        }






        for (Human human : humansWithNationalContacts) {
            int numberOfContacts = random.nextInt(maxContacts - minContacts + 1) + minContacts;
            Set<Integer> contacts = new HashSet<>();

            int country = human.getCountry();
            List<Integer> sameCountryHumans = countryHumanMap.get(country);

            int maxIterations = 50;
            int iterations = 0;

            while (contacts.size() + human.getContactList().size() < numberOfContacts && iterations < maxIterations) {
                int randomIndex = random.nextInt(sameCountryHumans.size());
                int contact = sameCountryHumans.get(randomIndex);

                if (contact != human.getValue() && !human.getContactList().containsKey(contact)) {
                    contacts.add(contact);
                }
                iterations++;
            }

            for (int contactValue : contacts) {
                int contactPeriod = random.nextInt(1440) + 1;
                human.getContactList().put(contactValue, contactPeriod);
                humans.get(contactValue - 1).getContactList().put(human.getValue(), contactPeriod);
            }

            if (human.getValue() % 100000 == 0) {
                System.out.println("Human " + human.getValue() + " has " + human.getContactList().size() + " contacts");
            }
        }










        //Collections.shuffle(humans);
        for (int i = 0; i < initiallyInfected; i++) {
            int randomIndex = random.nextInt(populationSize);
            humans.get(randomIndex).setState(State.INFECTIOUS);
        }

      /*  for (Human human : humans) {

            for (Integer key : human.getContactList().keySet()) {
                if (human.getCountry() != humans.get(key - 1).getCountry()) {
                   // System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                }
            }
        }*/

        return humans;
    }


    private long getRandomCountry(Map<Long, Double> countryProbabilities, Random random) {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Map.Entry<Long, Double> entry : countryProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        // Default return in case something goes wrong, should never hit this line
        return countryProbabilities.keySet().iterator().next();
    }


    public List<Integer> getPopulationByCountry(List<Human> humans) {
        int[] populationByCountry = new int[255];

        for (Human human : humans) {
            int countryId = human.getCountry();
            if (countryId >= 1 && countryId <= 255) {
                populationByCountry[countryId - 1]++;
            }
        }

        List<Integer> populationList = new ArrayList<>();
        for (int population : populationByCountry) {
            populationList.add(population);
        }

        return populationList;
    }


    public Population getPopulationBySimulation(Long simulationId){
        return populationRepository.getPopulationBySimulation(simulationId);
    }

}
