package org.example.repository.interfaces;

import org.example.domain.Result;

import java.util.List;

public interface IResultsRepository {

    Result saveResult(Result result);
    List<Result> getResultsBySimulation(Long simulation_id);

}
