package com.project.linguaitalianaua.repository;

import com.project.linguaitalianaua.model.Notification;
import com.project.linguaitalianaua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);
}
