package com.kjeldsen.match.application.events;

import com.kjeldsen.match.entities.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class MatchStartedEvent extends Event {

    Id matchId;
}
