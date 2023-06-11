package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.Word;
import com.project.linguaitalianaua.repository.SentencesRepository;
import com.project.linguaitalianaua.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SentenceService {

    private final SentencesRepository sentencesRepository;
    private final Random rand = new Random();

    @Autowired
    public SentenceService(SentencesRepository sentencesRepository) {
        this.sentencesRepository = sentencesRepository;
    }

    public Sentence save(Sentence sentence) {
        return sentencesRepository.save(sentence);
    }

    public void delete(Long id) {
        sentencesRepository.deleteById(id);
    }

    public Sentence findById(Long id) {
        return sentencesRepository.findById(id).orElse(null);
    }

    public Sentence getRandomSentence() {
        List<Sentence> sentences = sentencesRepository.findAll();
        if(sentences.isEmpty()){
            return null;
        }
        return sentences.get(rand.nextInt(sentences.size()));
    }
}
