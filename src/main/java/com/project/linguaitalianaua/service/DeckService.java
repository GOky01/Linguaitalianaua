package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Deck;
import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.model.Word;
import com.project.linguaitalianaua.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {
    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Deck findById(Long deckId) {
        return deckRepository.findById(deckId).orElse(null);
    }

    public List<Deck> findByUser(User user) {
        return deckRepository.findByUser(user);
    }

    public void addWord(Deck deck, String word) {
        deck.getWords().add(word);
        deckRepository.save(deck);
    }

    public void addSentence(Deck deck, String sentence) {
        deck.getSentences().add(sentence);
        deckRepository.save(deck);
    }

    public Deck createDeck(String name, String description, User user) {
        Deck deck = new Deck();
        deck.setName(name);
        deck.setDescription(description);
        deck.setUser(user);
        return deckRepository.save(deck);
    }

    public void deleteDeck(Long id) {
        deckRepository.deleteById(id);
    }

    public int getTotalDecksCount() {
        return (int) deckRepository.count();
    }
}
