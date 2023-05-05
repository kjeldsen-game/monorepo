package com.kjeldsen.player.kafka.events.body;

import com.kjeldsen.events.domain.Event;
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
