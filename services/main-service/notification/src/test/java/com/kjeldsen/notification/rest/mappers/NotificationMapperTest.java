package com.kjeldsen.notification.rest.mappers;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.rest.model.NotificationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    @ParameterizedTest
    @EnumSource(Notification.NotificationType.class)
    @DisplayName("Should map the type enum")
    void should_map_notification_type(Notification.NotificationType notificationType) {
        assertThat(NotificationMapper.INSTANCE.map(notificationType).toString())
            .isEqualTo(notificationType.toString());
    }

    @ParameterizedTest
    @EnumSource(Notification.NotificationType.class)
    @DisplayName("Should map notification to notification response")
    void should_map_notification_to_notification_response(Notification.NotificationType type) {
        NotificationResponse response = NotificationMapper.INSTANCE.map(
            Notification.builder()
                .id("id")
                .type(type)
                .message("message")
                .payload(Map.of("exampleKey", "exampleValue"))
                .isRead(false)
                .build()
        );

        assertNotNull(response);
        assertEquals("id", response.getId());
        assertEquals(type.toString(), response.getType().toString());
        assertEquals("message", response.getMessage());
        assertEquals(Map.of("exampleKey", "exampleValue"), response.getPayload());
    }


    @Test
    @DisplayName("Should map notifications list of notification response")
    void should_map_notifications_list_of_notifications_response() {
        List<Notification> notifications =  List.of(
            Notification.builder()
                .id("id1")
                .type(Notification.NotificationType.MATCH_END)
                .message("message")
                .payload(Map.of("exampleKey", "exampleValue"))
                .isRead(false)
                .build(),
            Notification.builder()
                .id("id2")
                .type(Notification.NotificationType.AUCTION_BID)
                .message("message2")
                .payload(Map.of("exampleKey2", "exampleValue2"))
                .isRead(false)
                .build()
        );

        List<NotificationResponse> response = NotificationMapper.INSTANCE.map(
            notifications);

        assertThat(response).hasSize(2);

        assertThat(response.get(0))
            .extracting("id", "message", "payload", "isRead")
            .containsExactly(
                "id1",
                "message",
                Map.of("exampleKey", "exampleValue"),
                false
            );

    }
}
