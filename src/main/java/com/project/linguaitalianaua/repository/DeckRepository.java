package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.Deck;
import com.project.linguaitalianaua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUser(User user);
}

