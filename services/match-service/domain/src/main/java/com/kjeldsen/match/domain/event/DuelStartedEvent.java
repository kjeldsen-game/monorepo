package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.DuelId;
import com.kjeldsen.match.domain.id.ModifierId;
import com.kjeldsen.match.domain.id.PlayId;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.PitchArea;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@SuperBuilder
public class DuelStartedEvent extends Event {

    private DuelId duelId;
    private PlayId playId;
    private TeamId attackingTeamId;
    private PlayerId attackingPlayerId;
    private TeamId defendingTeamId;
    private PlayerId defendingPlayerId;
    private Map<PlayerId, List<ModifierId>> modifiers;
    private PitchArea pitchArea;

}
