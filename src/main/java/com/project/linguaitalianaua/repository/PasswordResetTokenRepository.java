package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.PasswordResetToken;
import com.project.linguaitalianaua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    void deleteByToken(String token);
}
