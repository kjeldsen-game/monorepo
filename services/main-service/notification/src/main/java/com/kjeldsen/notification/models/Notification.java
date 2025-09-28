package com.kjeldsen.notification.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "Notifications")
@CompoundIndex(name = "user_read_idx", def = "{'teamId': 1, 'isRead': 1}")
public class Notification {

    @Id
    private String id;
    private String message;
    private Boolean isRead;
    private String teamId;
    private Instant createdAt;
    private Map<String, Object> payload;
    private NotificationType type;

    public enum NotificationType {
        AUCTION_BID,
        MATCH_END
    }
}
