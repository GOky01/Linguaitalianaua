package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.Result;
import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUser(User user);

    Result findByUserAndWord(User user, Word word);

    Result findByUserAndSentence(User user, Sentence sentence);
}
