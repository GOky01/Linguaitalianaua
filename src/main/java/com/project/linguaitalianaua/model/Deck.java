package com.project.linguaitalianaua.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ElementCollection
    private List<String> words = new ArrayList<>();

    @ElementCollection
    private List<String> sentences = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Deck deck = (Deck) o;
        return id != null && Objects.equals(id, deck.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

