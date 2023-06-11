package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
//    List<Achievement> findByRequiredLevelLessThanEqual(int level);
//
//    List<Achievement> findByRequiredPointsLessThanEqual(int points);
//
//    List<Achievement> findByRequiredExercisesLessThanEqual(int size);
}
