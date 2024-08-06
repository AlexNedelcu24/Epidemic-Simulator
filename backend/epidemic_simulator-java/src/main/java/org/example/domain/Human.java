package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "humans")
@AllArgsConstructor
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int value;
    private String simulationName;
    private int country;
    private int incubationPeriod;
    private int infectiousPeriod;

    @ElementCollection
    @CollectionTable(name = "contact_list", joinColumns = @JoinColumn(name = "human_id"))
    @MapKeyColumn(name = "contact")
    @Column(name = "weight")
    private Map<Integer, Integer> contactList;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @OneToOne
    @JoinColumn(name = "health_state_id")
    private HealthState healthState;

    public Human() {
    }

    public Human(int value, String simulationName, int country, int incubationPeriod, int infectiousPeriod, Map<Integer, Integer> contactList, State state) {
        this.value = value;
        this.simulationName = simulationName;
        this.country = country;
        this.incubationPeriod = incubationPeriod;
        this.infectiousPeriod = infectiousPeriod;
        this.contactList = contactList;
        this.state = state;
        healthState = new HealthState(0,0,0);
    }

    @Override
    public String toString() {
        return "Human{" +
                "value=" + value +
                ", country=" + country +
                ", incubationPeriod=" + incubationPeriod +
                ", infectiousPeriod=" + infectiousPeriod +
                ", state=" + state +
                '}';
    }
}