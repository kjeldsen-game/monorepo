package com.kjeldsen.notification.usecases;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.repositories.NotificationReadRepository;
import com.kjeldsen.notification.repositories.NotificationWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarkAsReadNotificationUseCase {

    private final NotificationReadRepository notificationReadRepository;
    private final NotificationWriteRepository notificationWriteRepository;

    public void markAsReadNotification(String notificationId, String teamId) {
        log.info("Marking notification {} as read", notificationId);
        Optional<Notification> notification = Optional.ofNullable(notificationReadRepository
            .findById(notificationId).orElseThrow(RuntimeException::new));

        if (notification.isPresent()) {
            if (!notification.get().getTeamId().equals(teamId)) {
                throw new IllegalArgumentException();
            }
            notification.get().setIsRead(true);
            notificationWriteRepository.save(notification.get());
        }
    }
}
