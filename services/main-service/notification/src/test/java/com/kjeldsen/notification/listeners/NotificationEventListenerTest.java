package com.kjeldsen.notification.listeners;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.notification.usecases.CreateNotificationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationEventListenerTest {

    private CreateNotificationUseCase createNotificationUseCase;
    private NotificationEventListener listener;

    @BeforeEach
    void setUp() {
        createNotificationUseCase = mock(CreateNotificationUseCase.class);
        listener = new NotificationEventListener(createNotificationUseCase);
    }

    @Test
    void shouldDelegateEventToUseCase() {
        NotificationEvent event = mock(NotificationEvent.class);
        listener.handleNotificationEvent(event);
        ArgumentCaptor<NotificationEvent> captor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(createNotificationUseCase, times(1)).create(captor.capture());
        assertThat(captor.getValue()).isEqualTo(event);
    }
}
