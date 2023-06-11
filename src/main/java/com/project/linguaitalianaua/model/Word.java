package com.project.linguaitalianaua.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String italian;
    private String ukrainian;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Word word = (Word) o;
        return id != null && Objects.equals(id, word.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
