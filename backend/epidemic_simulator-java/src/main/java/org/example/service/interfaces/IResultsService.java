package org.example.service.interfaces;

import org.example.domain.Result;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface IResultsService {

    Result saveResult(int day, int infectionsNumber, Long simulationId) throws ValidationException;
    List<Result> getResultsBySimulation(Long simulationId);

}
