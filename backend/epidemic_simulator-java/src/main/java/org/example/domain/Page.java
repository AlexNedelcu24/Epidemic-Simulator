package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pages")
@AllArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String title;
    private String text;
    private String link;


    public Page(String title, String text, String link) {
        this.title = title;
        this.text = text;
        this.link = link;
    }

    public Page() {

    }
}
