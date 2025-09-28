package com.kjeldsen.notification.usecases;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.notification.repositories.NotificationWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreateNotificationUseCaseTest {

    private final NotificationWriteRepository mockedNotificationWriteRepository = Mockito
            .mock(NotificationWriteRepository.class);
    private final CreateNotificationUseCase createNotificationUseCase = new CreateNotificationUseCase(
            mockedNotificationWriteRepository);

    @Test
    @DisplayName("Should create a notification object")
    void should_create_notification_object() {
        NotificationEvent event = NotificationEvent.builder()
                .message("message")
                .teamIds(List.of("team1", "team2"))
                .payload(Map.of())
                .type(NotificationEvent.NotificationEventType.AUCTION_BID)
                .build();

        createNotificationUseCase.create(event);

        verify(mockedNotificationWriteRepository, times(1)).saveAll(anyList());
    }
}