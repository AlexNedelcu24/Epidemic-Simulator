package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "simulations")
@AllArgsConstructor
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Long userId;

    private String name;

    private int transmissionProbability;



    public Simulation() {
    }

    public Simulation(Long userId, String name, int transmissionProbability) {
        this.userId = userId;
        this.name = name;
        this.transmissionProbability = transmissionProbability;
    }
}
