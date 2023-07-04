package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.MatchModifier;
import com.kjeldsen.match.domain.type.ModifierMetaInfo;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class MatchCreatedEvent extends Event {

    private MatchId matchId;
    private List<TeamId> teamIds;
    private List<ModifierMetaInfo<MatchModifier>> modifiers;

}
