package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.PasswordResetToken;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.repository.PasswordResetTokenRepository;
import com.project.linguaitalianaua.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public void save(@NotNull User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void updatePassword(@NotNull User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(myToken);
    }

    public PasswordResetToken getPasswordResetToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deletePasswordResetToken(String token) {
        tokenRepository.deleteByToken(token);
    }

}

