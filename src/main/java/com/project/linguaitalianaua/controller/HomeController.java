package com.project.linguaitalianaua.controller;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.project.linguaitalianaua.model.*;
import com.project.linguaitalianaua.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.cloud.translate.Translate.TranslateOption.sourceLanguage;
import static com.google.cloud.translate.Translate.TranslateOption.targetLanguage;

@Controller
public class HomeController {

    private static final int BASE_POINTS = 10;

    private static final double LEVEL_FACTOR = 1.5;

    private static final String API_KEY = "AIzaSyB6-TogppnlaRKBxmpywG-__rgi1XG1T0A";

    private final Translate translate = TranslateOptions.newBuilder().setApiKey(API_KEY).build().getService();

    private final WordService wordService;

    private final SentenceService sentenceService;

    private final UserService userService;

    private final ResultService resultService;

    private final DeckService deckService;

    private final NotificationService notificationService;

    private final AchievementService achievementService;

    public HomeController(WordService wordService, SentenceService sentenceService, UserService userService, ResultService resultService, DeckService deckService,
                          NotificationService notificationService, AchievementService achievementService) {
        this.wordService = wordService;
        this.sentenceService = sentenceService;
        this.userService = userService;
        this.resultService = resultService;
        this.deckService = deckService;
        this.notificationService = notificationService;
        this.achievementService = achievementService;
    }


    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());

        int pointsForNextLevel = (int) Math.pow(LEVEL_FACTOR, user.getLevel()) * BASE_POINTS;

        model.addAttribute("user", user);
        model.addAttribute("pointsForNextLevel", pointsForNextLevel);
        return "profile";
    }

    private boolean hasAchievement(User user, String achievementName) {
        return user.getAchievements().stream()
                .anyMatch(achievement -> achievement.getName().equals(achievementName));
    }

    public static int calculateLevel(int points) {
        int level = 0;
        double pointsForNextLevel = BASE_POINTS;

        while (points >= pointsForNextLevel) {
            level++;
            points -= pointsForNextLevel;
            pointsForNextLevel *= LEVEL_FACTOR;
        }

        return level;
    }

    @GetMapping("/practice")
    public String practice(Model model, @RequestParam(required = false) Integer exerciseNumber, Principal principal) {
        if (exerciseNumber == null) {
            exerciseNumber = 1;
        }

        for (int i = 1; i <= 5; i++) {
            String resultAttributeName = "exercise" + i + "Result";
            if (model.containsAttribute(resultAttributeName)) {
                model.addAttribute(resultAttributeName, model.getAttribute(resultAttributeName));
            }
        }

        if (exerciseNumber >= 1 && exerciseNumber <= 5) {
            model.addAttribute("word", wordService.getRandomWord());
            model.addAttribute("sentence", sentenceService.getRandomSentence());
        }

        model.addAttribute("exerciseNumber", exerciseNumber);

        return "practice";
    }

    @PostMapping("/check-answer")
    public String checkAnswer(@RequestParam String type, @RequestParam(required = false) Long id, @RequestParam String answer,
                              Principal principal, RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(principal.getName());
        Word word = wordService.findById(id);
        Sentence sentence = sentenceService.findById(id);
        boolean isCorrect = false;

        if (type.equalsIgnoreCase("exercise1") || type.equalsIgnoreCase("exercise2")) {
            String wordItalian = word.getItalian();
            String wordUkrainian = word.getUkrainian();
            isCorrect = wordItalian.equalsIgnoreCase(answer.trim()) || translate.translate(wordUkrainian,
                    sourceLanguage("uk"), targetLanguage("it")).getTranslatedText().equalsIgnoreCase(answer.trim());
            redirectAttributes.addFlashAttribute("exercise" + type.substring(8) + "Result", isCorrect ? "correct" : "incorrect");
            resultService.saveOrUpdate(user, word, isCorrect);
            user.setPoints(user.getPoints() + (isCorrect ? Integer.parseInt(type.substring(8)) : 0));
        } else if (type.equalsIgnoreCase("exercise3") || type.equalsIgnoreCase("exercise4") || type.equalsIgnoreCase("exercise5")) {
            String sentenceItalian = sentence.getItalian();
            String sentenceUkrainian = sentence.getUkrainian();
            isCorrect = sentenceItalian.equalsIgnoreCase(answer) || translate.translate(sentenceUkrainian, sourceLanguage("uk"), targetLanguage("it")).getTranslatedText().equalsIgnoreCase(answer);
            redirectAttributes.addFlashAttribute("exercise" + type.substring(8) + "Result", isCorrect ? "correct" : "incorrect");
            resultService.saveOrUpdateSentence(user, sentence, isCorrect);
            user.setPoints(user.getPoints() + (isCorrect ? Integer.parseInt(type.substring(8)) + 2 : 0));
        }

        user.setLevel(calculateLevel(user.getPoints()));

        List<Achievement> achievements = new ArrayList<>();
        int[] levelThresholds = {2, 5, 10, 20, 50};
        int[] pointsThresholds = {20, 50, 100, 200};
        if (user.getLevel() == 1 && !hasAchievement(user, "Level 1 Achievement")) {
            Achievement levelAchievement = createAchievement("Level 1 Achievement", "Reached level 1");
            achievementService.save(levelAchievement);
            achievements.add(levelAchievement);

            Notification notification = new Notification();
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage("You have just received your first level");
            notification.setUser(user);
            notificationService.update(notification);
        }
        if (user.getPoints() >= 10 && user.getPoints() < 20 && !hasAchievement(user, "First 10 Points")) {
            Achievement levelAchievement = createAchievement("First 10 Points", "You`re on your way! First 10 Points acquired");
            achievementService.save(levelAchievement);
            achievements.add(levelAchievement);

            Notification notification = new Notification();
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage("You have just received your first points");
            notification.setUser(user);
            notificationService.update(notification);
        }

        for (int threshold : levelThresholds) {
            if (user.getLevel() >= threshold && !hasAchievement(user, "Level " + threshold + " Achievement")) {
                Achievement levelAchievement = createAchievement("Level " + threshold + " Achievement", "Reached level " + threshold);
                achievementService.save(levelAchievement);
                achievements.add(levelAchievement);
            }
        }

        for (int threshold : pointsThresholds) {
            if (user.getPoints() >= threshold && !hasAchievement(user, "Points " + threshold + " Acquired")) {
                Achievement pointAchievement = createAchievement("Points " + threshold + " Acquired", "Acquired " + threshold + " Points");
                achievementService.save(pointAchievement);
                achievements.add(pointAchievement);
            }
        }
        user.getAchievements().addAll(achievements);
        userService.update(user);
        int exerciseNumber = Integer.parseInt(type.substring(8,9));
        if (isCorrect) {
            exerciseNumber++;
        }
        if (exerciseNumber > 5) {
            if(!hasAchievement(user, "Перші 5 вправ!")) {
                Achievement levelAchievement = createAchievement("Перші 5 вправ!", "Бум! Ви пройшли перші 5 вправ - ви молодець! Продовжуйте в тому ж русі");
                achievementService.save(levelAchievement);
                user.getAchievements().add(levelAchievement);
            }
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("exerciseNumber", exerciseNumber);
        }

        return "redirect:/practice?exerciseNumber=" + exerciseNumber;
    }

    @GetMapping("/notifications")
    public String notifications(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("notifications", notificationService.findUnreadByUser(user));
        return "notifications";
    }

    @PostMapping("/notifications/{notificationId}/mark-as-read")
    public String markAsRead(@PathVariable Long notificationId, RedirectAttributes redirectAttributes) {
        Notification notification = notificationService.findById(notificationId);
        if (notification != null) {
            notification.setRead(true);
            notification.setCreatedAt(LocalDateTime.now());
            notificationService.update(notification);
            redirectAttributes.addFlashAttribute("message", "Notification marked as read");
        }
        return "redirect:/notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllAsRead(Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());
        List<Notification> notifications = notificationService.findUnreadByUser(user);
        for (Notification notification : notifications) {
            if (notification.getIsRead()) {
                notification.setRead(true);
                notification.setCreatedAt(LocalDateTime.now());
                notificationService.update(notification);
            }
        }
        redirectAttributes.addFlashAttribute("message", "All notifications marked as read");
        return "redirect:/notifications";
    }

    @GetMapping("/decks")
    public String decks(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("decks", deckService.findByUser(user));
        return "deck";
    }

    @PostMapping("/decks/{deckId}/add-word")
    public String addWordToDeck(@PathVariable Long deckId,
                                @RequestParam String word,
                                RedirectAttributes redirectAttributes) {
        Deck deck = deckService.findById(deckId);
        if (deck != null) {
            deckService.addWord(deck, word);
            redirectAttributes.addFlashAttribute("word", "Word added successfully");
        }
        return "redirect:/decks";
    }

    @PostMapping("/decks/{deckId}/add-sentence")
    public String addSentenceToDeck(@PathVariable Long deckId, @RequestParam String sentence, RedirectAttributes redirectAttributes) {
        Deck deck = deckService.findById(deckId);
        if (deck != null) {
            deckService.addSentence(deck, sentence);
            redirectAttributes.addFlashAttribute("sentence", "Sentence added successfully");
        }
        return "redirect:/decks";
    }

    public Achievement createAchievement(String name, String description) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setAchievedAt(LocalDateTime.now());
        achievement.setUnlocked(true);
        return achievement;
    }

    @PostMapping("/decks/create")
    public String createDeck(
            @RequestParam String name,
            @RequestParam String description,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(principal.getName());
        deckService.createDeck(name, description, user);

        int createdDecksCount = deckService.getTotalDecksCount();

        if (createdDecksCount == 1) {
            Achievement decksCreated = createAchievement("First Deck", "Yahoo! Your first deck created successfully");
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage("You have created your first deck successfully");
            notification.setUser(user);
            notificationService.update(notification);
            achievementService.save(decksCreated);
            user.addAchievement(decksCreated);
        }

        if (createdDecksCount >= 5 && createdDecksCount % 5 == 0) {
            Achievement fiveDecksCreated = createAchievement("Five Decks Created", "Created " + createdDecksCount + " decks");
            achievementService.save(fiveDecksCreated);
            user.addAchievement(fiveDecksCreated);
        }

        if (createdDecksCount >= 10 && createdDecksCount % 10 == 0) {
            Achievement tenDecksCreated = createAchievement("Ten Decks Created", "Created " + createdDecksCount + " decks");
            achievementService.save(tenDecksCreated);
            user.addAchievement(tenDecksCreated);
        }

        redirectAttributes.addFlashAttribute("message", "Deck created successfully");
        return "redirect:/decks";
    }

    @PostMapping("/decks/{id}/delete")
    public String deleteDeck(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        deckService.deleteDeck(id);
        redirectAttributes.addFlashAttribute("message", "Deck deleted successfully");
        return "redirect:/decks";
    }

    @GetMapping("/statistics")
    public String stats(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("results", resultService.findByUser(user));
        return "statistics";
    }

    @GetMapping("/achievements")
    public String showAchievements(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        List<Achievement> achievements = achievementService.getAchievementsByUser(user);
        model.addAttribute("achievements", achievements);
        return "achievements";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping(value = "/export-results", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportResultsCsv(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Result> results = resultService.findByUser(user);

        String csvData = generateCsv(results);

        ByteArrayResource resource = new ByteArrayResource(csvData.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=results.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    private String generateCsv(List<Result> results) {
        StringBuilder csvData = new StringBuilder();
        csvData.append("id,ukrainian,italian,correct_attempts,incorrect_attempts,attempts\n");
        for (Result result : results) {
            csvData.append(String.join(",",
                    String.valueOf(result.getId()),
                    result.getWord().getUkrainian(),
                    result.getWord().getItalian(),
                    String.valueOf(result.getCorrectAnswers()),
                    String.valueOf(result.getIncorrectAnswers()),
                    String.valueOf(result.getAttempts()))
            );
            csvData.append("\n");
        }
        return csvData.toString();
    }
}
