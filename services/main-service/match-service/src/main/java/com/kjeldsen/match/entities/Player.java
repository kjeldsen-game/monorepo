package com.kjeldsen.match.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
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

    String id;
    String teamId;

    String name;
    PlayerPosition position;
    Map<PlayerSkill, Integer> skills;
    PlayerOrder playerOrder;

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role) {
        // TODO - refactor when implementing goalkeeper skills properly
        try {
            return skills.get(duelType.requiredSkill(role));
        } catch (NullPointerException e) {
            // Error might be thrown because the goalkeeper is attempting to pass
            if (duelType == DuelType.PASSING && role == DuelRole.INITIATOR) {
                return skills.get(PlayerSkill.ORGANIZATION);
            }
            throw e;
        }
    }

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
