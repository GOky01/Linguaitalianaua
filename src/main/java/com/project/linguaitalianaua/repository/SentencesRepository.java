package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentencesRepository extends JpaRepository<Sentence, Long> {
}
