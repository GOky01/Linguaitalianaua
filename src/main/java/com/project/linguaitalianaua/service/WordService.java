package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.Word;
import com.project.linguaitalianaua.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final Random rand = new Random();

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Word findById(Long id) {
        return wordRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        wordRepository.deleteById(id);
    }

    public Word getRandomWord() {
        List<Word> words = wordRepository.findAll();
        if(words.isEmpty()){
            return null;
        }
        return words.get(rand.nextInt(words.size()));
    }

}
