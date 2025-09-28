package com.kjeldsen.notification.persistence.adapters.mongo;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.persistence.mongo.repositories.NotificationMongoRepository;
import com.kjeldsen.notification.repositories.NotificationReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationReadRepositoryMongoAdapter implements NotificationReadRepository {

    private final NotificationMongoRepository notificationMongoRepository;

    @Override
    public List<Notification> findByTeamIdAndIsReadOrderByCreatedAtDesc(String teamId, boolean isRead) {
        return notificationMongoRepository.findByTeamIdAndIsReadOrderByCreatedAtDesc(teamId, isRead);
    }

    @Override
    public Optional<Notification> findById(String notificationId) {
        return notificationMongoRepository.findById(notificationId);
    }
}
