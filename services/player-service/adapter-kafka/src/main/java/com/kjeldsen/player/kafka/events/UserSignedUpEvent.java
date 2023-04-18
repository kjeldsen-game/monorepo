package com.kjeldsen.player.kafka.events;

import com.kjeldsen.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignedUpEvent extends Event {
    private String userId;

    private String teamName;
}
