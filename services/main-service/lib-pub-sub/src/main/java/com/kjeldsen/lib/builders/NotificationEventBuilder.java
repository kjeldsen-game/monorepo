package com.kjeldsen.lib.builders;

import com.kjeldsen.lib.events.NotificationEvent;

import java.util.List;
import java.util.Map;

public class NotificationEventBuilder {

    protected static NotificationEvent buildBaseEvent(
        List<String> teamIds,
        Map<String, Object> payload,
        String message,
        NotificationEvent.NotificationEventType type) {

        try {
            if (teamIds == null || teamIds.isEmpty()) {
                throw new IllegalArgumentException("teamIds cannot be null or empty");
            }

            return NotificationEvent.builder()
                .teamIds(teamIds)
                .payload(payload)
                .message(message)
                .type(type)
                .isRead(false)
                .build();
        } catch (IllegalArgumentException e) {
            System.err.println("NotificationEvent not created: " + e.getMessage());
            return null;
        }
    }
}
