package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.PlayerPosition;
import com.kjeldsen.match.domain.type.PlayerSkill;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    PlayerId id;
    String name;
    PlayerPosition position;
    Map<PlayerSkill, Integer> skillSet;
    TeamId teamId;

    public Integer getSkillPoints(PlayerSkill skill) {
        return skillSet.get(skill);
    }
}
