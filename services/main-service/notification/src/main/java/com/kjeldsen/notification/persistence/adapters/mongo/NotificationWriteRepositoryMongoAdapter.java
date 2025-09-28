package com.kjeldsen.notification.persistence.adapters.mongo;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.persistence.mongo.repositories.NotificationMongoRepository;
import com.kjeldsen.notification.repositories.NotificationWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationWriteRepositoryMongoAdapter implements NotificationWriteRepository {

    private final NotificationMongoRepository notificationMongoRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationMongoRepository.save(notification);
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        notificationMongoRepository.saveAll(notifications);
    }
}
