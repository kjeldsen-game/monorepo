package com.kjeldsen.notification.listeners;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.notification.usecases.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final CreateNotificationUseCase createNotificationUseCase;

    @EventListener
    @Async
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        log.info("NotificationEventListener received: {}", notificationEvent);
        createNotificationUseCase.create(notificationEvent);
    }
}
