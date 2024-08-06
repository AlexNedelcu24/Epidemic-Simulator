package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.Intervention;
import org.example.domain.Result;
import org.example.repository.ResultRepository;
import org.example.repository.SimulationRepository;
import org.example.repository.interfaces.IResultsRepository;
import org.example.repository.interfaces.ISimulationRepository;
import org.example.service.interfaces.IResultsService;

import javax.xml.bind.ValidationException;
import java.util.List;

@org.springframework.stereotype.Service(value = "resultService")
@AllArgsConstructor
public class ResultService implements IResultsService {

    private final ISimulationRepository simulationRepository;
    private final IResultsRepository resultRepository;

    public Result saveResult(int day, int infectionsNumber, Long simulationId) throws ValidationException {
        try {
            Result result = new Result(day, infectionsNumber, simulationId);

            if (simulationRepository.getSimulationById(simulationId) == null) {
                throw new ValidationException("There is no simulation for this result!");
            }

            return resultRepository.saveResult(result);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }


    public List<Result> getResultsBySimulation(Long simulationId){
        return resultRepository.getResultsBySimulation(simulationId);
    }
}
