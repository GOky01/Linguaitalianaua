package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Notification;
import com.project.linguaitalianaua.model.User;
import com.project.linguaitalianaua.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }

    public List<Notification> findUnreadByUser(User user) {
        List<Notification> allNotifications = notificationRepository.findByUser(user);
        return allNotifications.stream()
                .filter(notification -> notification.getIsRead())
                .collect(Collectors.toList());
    }

    public void update(Notification notification) {
        notificationRepository.save(notification);
    }
}

