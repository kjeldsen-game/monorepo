package com.kjeldsen.match.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerReceptionPreference;
import com.kjeldsen.player.domain.PlayerSkill;

import java.util.List;
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
    PlayerReceptionPreference receptionPreference;

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role, BallState ballState) {
        List<PlayerSkill> requiredSkills = duelType.requiredSkills(role, ballState);
        Integer skillValue = 0;

        if (!PlayerPosition.GOALKEEPER.equals(this.getPosition())) {
            if (requiredSkills.size() == 1) {
                PlayerSkill requiredSkill = requiredSkills.get(0);
                Optional<Integer> value = Optional.ofNullable(skills.get(requiredSkill));
                if (value.isPresent()) {
                    skillValue = value.get();
                }
            } else {
                // If more than one player skill is used on the calculation, for now just get the average.
                int average = 0;
                int consideredSkillsCounter = 0;
                for (PlayerSkill requiredSkill : requiredSkills) {
                    Optional<Integer> value = Optional.ofNullable(skills.get(requiredSkill));
                    if (value.isPresent()) {
                        average += value.get();
                        consideredSkillsCounter++;
                    }
                }
                if (consideredSkillsCounter > 0) {
                    skillValue = average / consideredSkillsCounter;
                }
            }
        } else {
            // A value may not be present if the player is a goalkeeper, since goalkeepers have a
            // different set of skills.
            // TOD - refactor when implementing goalkeeper skills properly
            if ((duelType == DuelType.PASSING_LOW || duelType == DuelType.PASSING_HIGH) && role == DuelRole.INITIATOR) {
                // Here the goalkeeper is attempting to pass the ball, but does not have the passing
                // skill. In this case, use the `organization` skill.
                skillValue = skills.get(PlayerSkill.ORGANIZATION);
            } else if (duelType == DuelType.LOW_SHOT && role == DuelRole.CHALLENGER) {
                // Here the goalkeeper is attempting to save a shot - this requires the reflexes`
                // skill.
                skillValue = skills.get(PlayerSkill.REFLEXES);
            }
        }

        if (skillValue == null) throw new RuntimeException("Player " + this.getName() + " does not have any of the required skills: " + requiredSkills);

        return skillValue;
    }


    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
