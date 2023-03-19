package com.kjeldsen.auth.kafka.events;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.events.Event;
import com.kjeldsen.events.EventId;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@Getter
public class UserSignedUpEvent extends Event {

    public static UserSignedUpEvent from(SignUp signUp) {
        return UserSignedUpEvent.builder()
                .id(EventId.generate())
                .occurredAt(Instant.now())
                .userId(signUp.getId())
                .teamName(signUp.getTeamName())
                .build();
    }

    private String userId;
    private String teamName;
}
