package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String email;
    private String username;
    private String password;
    @Column(name = "has_paused")
    private Boolean hasPaused;
    @Column(name = "infections_number")
    private Boolean infectionsNumber;


    public User(String username, String email, String password, Boolean hasPaused, Boolean infectionsNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.hasPaused = hasPaused;
        this.infectionsNumber = infectionsNumber;

    }

    public User() {

    }
}
