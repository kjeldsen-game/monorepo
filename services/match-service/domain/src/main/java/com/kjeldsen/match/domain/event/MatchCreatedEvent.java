package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.TeamId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class MatchCreatedEvent extends Event {

    MatchId matchId;
    TeamId homeTeamId;
    TeamId awayTeamId;

    // List<ModifierMetaInfo<MatchModifier>> modifiers;
}
