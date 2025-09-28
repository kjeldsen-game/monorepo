package com.kjeldsen.notification.usecases;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.repositories.NotificationReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetNotificationUseCase {

    private final NotificationReadRepository notificationReadRepository;

    public List<Notification> get(String teamId) {
        log.info("Get notifications by teamId {}", teamId);
        return notificationReadRepository.findByTeamIdAndIsReadOrderByCreatedAtDesc(teamId, false);
    }
}
