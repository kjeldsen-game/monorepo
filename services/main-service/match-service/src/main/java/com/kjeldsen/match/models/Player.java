package com.kjeldsen.match.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.modifers.PlayerOrder;
import com.kjeldsen.match.utils.JsonUtils;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    Long id;

    String name;
    PlayerPosition position;
    Map<SkillType, Integer> skills;
    PlayerOrder playerOrder;
    Team team;

    @SuppressWarnings("unused")
    public Long getTeamId() {
        if (team != null) {
            return team.getId();
        } else {
            return null;
        }
    }


    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role) {
        return skills.get(duelType.requiredSkill(role));
    }

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
