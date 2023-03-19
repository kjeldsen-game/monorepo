package com.kjeldsen.player.kafka.events;

import com.kjeldsen.events.Event;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserSignedUpEvent extends Event {
    private String userId;

    private String teamName;
}
