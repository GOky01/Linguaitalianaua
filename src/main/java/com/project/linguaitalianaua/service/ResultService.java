package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Result;
import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.model.Word;
import com.project.linguaitalianaua.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Result save(Result result) {
        return resultRepository.save(result);
    }

    public void delete(Result result) {
        resultRepository.delete(result);
    }

    public Result findResultById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    public List<Result> findAll() {
        return (List<Result>) resultRepository.findAll();
    }

    public List<Result> findByUser(User user) {
        return resultRepository.findByUser(user);
    }

    public Result update(Result result) {
        return resultRepository.save(result);
    }

    public void saveOrUpdate(User user, Word word, boolean isCorrect) {
        Result result = resultRepository.findByUserAndWord(user, word);
        if(result == null) {
            result = new Result();
            result.setUser(user);
            result.setWord(word);
        }
        result.setAttempts(result.getAttempts() + 1);
        if(isCorrect) {
            result.setCorrectAnswers(result.getCorrectAnswers() + 1);
        } else result.setIncorrectAnswers(result.getIncorrectAnswers() + 1);
        resultRepository.save(result);
    }

    public void saveOrUpdateSentence(User user, Sentence sentence, boolean isCorrect) {
        Result result = resultRepository.findByUserAndSentence(user, sentence);
        if(result == null) {
            result = new Result();
            result.setUser(user);
            result.setSentence(sentence);
        }
        result.setAttempts(result.getAttempts() + 1);
        if(isCorrect) {
            result.setCorrectAnswers(result.getCorrectAnswers() + 1);
        } else result.setIncorrectAnswers(result.getIncorrectAnswers() + 1);
        resultRepository.save(result);
    }
}
