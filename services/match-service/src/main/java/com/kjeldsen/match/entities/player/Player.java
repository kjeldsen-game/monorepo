package com.kjeldsen.match.entities.player;

import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.duel.DuelType;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    Id id;
    String name;
    PlayerPosition position;
    Map<PlayerSkill, Integer> skillSet;
    Id teamId;

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role) {
        return skillSet.get(duelType.requiredSkill(role));
    }
}
