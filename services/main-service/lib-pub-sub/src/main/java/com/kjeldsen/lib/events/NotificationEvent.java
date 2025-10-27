package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@SuperBuilder
public class NotificationEvent extends Event {
    String message;
    Boolean isRead;
    List<String> teamIds;
    Map<String, Object> payload;
    NotificationEventType type;

    public enum NotificationEventType {
        AUCTION_BID,
        MATCH_END,
        LEAGUE_START
    }
}
