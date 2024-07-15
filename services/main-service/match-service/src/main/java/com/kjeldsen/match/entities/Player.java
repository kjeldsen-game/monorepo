package com.kjeldsen.match.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.Map;
import java.util.Optional;

import com.kjeldsen.player.domain.PlayerStatus;
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
    PlayerStatus status;
    Map<PlayerSkill, Integer> skills;
    PlayerOrder playerOrder;

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role) {
        PlayerSkill requiredSkill = duelType.requiredSkill(role);
        Optional<Integer> value = Optional.ofNullable(skills.get(requiredSkill));
        if (value.isPresent()) {
            return value.get();
        }

        // A value may not be present if the player is a goalkeeper, since goalkeepers have a
        // different set of skills.
        // TOD - refactor when implementing goalkeeper skills properly
        if (duelType == DuelType.PASSING && role == DuelRole.INITIATOR) {
            // Here the goalkeeper is attempting to pass the ball, but does not have the passing
            // skill. In this case, use the `organization` skill.
            return skills.get(PlayerSkill.ORGANIZATION);
        } else if (duelType == DuelType.SHOT && role == DuelRole.CHALLENGER) {
            // Here the goalkeeper is attempting to save a shot - this requires the `reflexes`
            // skill.
            return skills.get(PlayerSkill.REFLEXES);
        } else {
            throw new RuntimeException("Player does not have the required skill: " + requiredSkill);
        }
    }


    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
