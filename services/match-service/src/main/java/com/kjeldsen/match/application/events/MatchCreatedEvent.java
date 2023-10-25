package com.kjeldsen.match.application.events;

import com.kjeldsen.match.entities.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class MatchCreatedEvent extends Event {

    Id matchId;
    Id homeTeamId;
    Id awayTeamId;
}
