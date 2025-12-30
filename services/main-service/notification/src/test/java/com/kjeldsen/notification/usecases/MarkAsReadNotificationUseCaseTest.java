package com.kjeldsen.notification.usecases;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.repositories.NotificationReadRepository;
import com.kjeldsen.notification.repositories.NotificationWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;


@ExtendWith(MockitoExtension.class)
class MarkAsReadNotificationUseCaseTest {

    @Mock
    private NotificationReadRepository mockedNotificationReadRepository;
    @Mock
    private NotificationWriteRepository mockedNotificationWriteRepository;
    @InjectMocks
    private MarkAsReadNotificationUseCase markAsReadNotificationUseCase;


    @Test
    @DisplayName("Should throw an error when notification id is invalid")
    void should_throw_error_when_notification_id_is_invalid() {
        Mockito.when(mockedNotificationReadRepository.findById("invalidId"))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> markAsReadNotificationUseCase.markAsReadNotification("invalidId", "teamId"));
    }

    @Test
    @DisplayName("Should throw an error when team id does not match")
    void should_throw_error_when_team_id_does_not_match() {
        Notification notification =  Notification.builder()
            .id("validId")
            .teamId("teamId")
            .isRead(false)
            .type(Notification.NotificationType.AUCTION_BID)
            .build();

        Mockito.when(mockedNotificationReadRepository.findById("validId"))
            .thenReturn(Optional.of(notification));
        assertThrows(IllegalArgumentException.class,
            () -> markAsReadNotificationUseCase.markAsReadNotification("validId", "wrongIt"));
    }

    @Test
    @DisplayName("Should mark notification as read and save")
    void should_mark_notification_as_read_and_save() {
        Notification notification =  Notification.builder()
            .id("validId")
            .teamId("teamId")
            .isRead(false)
            .type(Notification.NotificationType.AUCTION_BID)
            .build();
        Mockito.when(mockedNotificationReadRepository.findById("validId")).thenReturn(Optional.of(notification));
        markAsReadNotificationUseCase.markAsReadNotification("validId", "teamId");

        assertThat(notification.getIsRead()).isTrue();
        Mockito.verify(mockedNotificationWriteRepository, Mockito.times(1)).save(notification);
    }
}