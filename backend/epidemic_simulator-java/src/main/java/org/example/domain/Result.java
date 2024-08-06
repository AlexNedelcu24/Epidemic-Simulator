package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "results")
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int day;
    private int infectionsNumber;
    private Long simulationId;


    public Result(int day, int infectionsNumber, Long simulationId) {
        this.day = day;
        this.infectionsNumber = infectionsNumber;
        this.simulationId = simulationId;
    }

    public Result() {

    }
}
