package com.kjeldsen.notification.repositories;

import com.kjeldsen.notification.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationReadRepository {

    List<Notification> findByTeamIdAndIsReadOrderByCreatedAtDesc(String teamId, boolean isRead);

    Optional<Notification> findById(String notificationId);
}
