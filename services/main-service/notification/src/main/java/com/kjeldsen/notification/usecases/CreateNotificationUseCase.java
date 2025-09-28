package com.kjeldsen.notification.usecases;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.repositories.NotificationWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNotificationUseCase {

    private final NotificationWriteRepository notificationWriteRepository;

    public void create(NotificationEvent event) {
        List<Notification> notifications = event.getTeamIds().stream()
            .map(teamId -> Notification.builder()
                .payload(event.getPayload())
                .createdAt(Instant.now())
                .id(UUID.randomUUID().toString())
                .isRead(false)
                .type(Notification.NotificationType.valueOf(event.getType().toString()))
                .message(event.getMessage())
                .teamId(teamId)
                .build()
            )
            .toList();

        notificationWriteRepository.saveAll(notifications);
    }
}
