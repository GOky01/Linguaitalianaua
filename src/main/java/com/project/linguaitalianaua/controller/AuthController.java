package com.project.linguaitalianaua.controller;

import com.project.linguaitalianaua.model.Notification;
import com.project.linguaitalianaua.model.PasswordResetToken;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.service.EmailService;
import com.project.linguaitalianaua.service.NotificationService;
import com.project.linguaitalianaua.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller

public class AuthController {

    private final UserService userService;

    private final NotificationService notificationService;

    private final EmailService emailService;

    public AuthController(UserService userService, NotificationService notificationService, EmailService emailService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String auth() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult bindingResult) {
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username.");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
        }
        if (user.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "error.user", "Password must be at least 8 characters long.");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(user);
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage("Вітаю! Ви успішно створили обліковий запис");
        notification.setUser(user);
        notificationService.update(notification);

        Notification notification2 = new Notification();
        notification2.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification2.setMessage("Тепер ви можете отримати доступ до сторінки користувача");
        notification2.setUser(user);
        notificationService.update(notification2);

        Notification notification3 = new Notification();
        notification3.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification3.setMessage("Тренеруйтесь на сторінці Практики");
        notification3.setUser(user);
        notificationService.update(notification3);

        Notification notification4 = new Notification();
        notification4.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification4.setMessage("Створюйте власні колоди слів з назвою(категоріями) та описом");
        notification4.setUser(user);
        notificationService.update(notification4);

        Notification notification5 = new Notification();
        notification5.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification5.setMessage("Переглядайте сторінку статистики");
        notification5.setUser(user);
        notificationService.update(notification5);

        Notification notification6 = new Notification();
        notification6.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification6.setMessage("Подивіться на свої досягнення!");
        notification6.setUser(user);
        notificationService.update(notification6);

        Notification notification7 = new Notification();
        notification7.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification7.setMessage("Слідкуйте за сповіщеннями системи");
        notification7.setUser(user);
        notificationService.update(notification7);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser() {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping("/forgot")
    public String displayForgotPasswordForm() {
        return "forgot";
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null){
            model.addAttribute("message", "We didn't find an account for that e-mail address.");
        } else {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" +
                    "http://localhost:8080/reset-password/" + token);
            emailService.sendEmail(passwordResetEmail);
            model.addAttribute("message", "A password reset link has been sent to " + email);
        }
        return "forgot";
    }

    @GetMapping("/reset-password/{token}")
    public String showResetPasswordForm(@PathVariable String token, Model model) {
        PasswordResetToken resetToken = userService.getPasswordResetToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("message", "Invalid or expired password reset token.");
        } else {
            model.addAttribute("token", token);
        }
        return "resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = userService.getPasswordResetToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            return "redirect:/reset-password?error=Invalid or expired password reset token.";
        } else {
            User user = resetToken.getUser();
            userService.updatePassword(user, newPassword);
            userService.deletePasswordResetToken(resetToken.getToken());
            return "redirect:/login?message=Password has been reset successfully. You can now log in with the new password.";
        }
    }


}
