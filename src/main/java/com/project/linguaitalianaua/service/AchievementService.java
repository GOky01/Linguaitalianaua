package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Achievement;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    public void unlockAchievements(User user, List<Achievement> achievements) {
        for (Achievement achievement : achievements) {
            if (!user.getAchievements().contains(achievement)) {
                achievement.setAchievedAt(LocalDateTime.now());
                achievement.getUsers().add(user);
                user.getAchievements().add(achievement);
                achievementRepository.save(achievement);
            }
        }
    }

    // Конструктор та інші залежності

//    public void unlockAchievements(User user) {
//        List<Achievement> unlockedAchievements = new ArrayList<>();
//
//        // Перевірка досягнень, що базуються на рівні
//        List<Achievement> levelAchievements = achievementRepository.findByRequiredLevelLessThanEqual(user.getLevel());
//        unlockedAchievements.addAll(levelAchievements);
//
//        // Перевірка досягнень, що базуються на очках
//        List<Achievement> pointsAchievements = achievementRepository.findByRequiredPointsLessThanEqual(user.getPoints());
//        unlockedAchievements.addAll(pointsAchievements);
//
//        // Перевірка досягнень, що базуються на пройдених вправах
//        List<Achievement> exercisesAchievements = achievementRepository.findByRequiredExercisesLessThanEqual(user.getAchievements().size());
//        unlockedAchievements.addAll(exercisesAchievements);
//
//        // Додавання розблокованих досягнень користувачу
//        for (Achievement achievement : unlockedAchievements) {
//            if (!user.getAchievements().contains(achievement)) {
//                achievement.setUnlocked(true);
//                achievement.setAchievedAt(LocalDateTime.now());
//                user.addAchievement(achievement);
//                achievementRepository.save(achievement);
//            }
//        }
//    }

    public List<Achievement> getAchievementsByUser(User user) {
        return user.getAchievements();
    }

    public void save(Achievement deckCreated) {
        achievementRepository.save(deckCreated);
    }

}

