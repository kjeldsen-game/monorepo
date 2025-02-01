package com.kjeldsen.match.domain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.recorder.GameProgressRecord;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.utils.JsonUtils;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerReceptionPreference;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    TeamRole teamRole;

    String name;
    PlayerPosition position;
    PlayerStatus status;
    Map<PlayerSkill, Integer> skills;
    PlayerOrder playerOrder;
    PitchArea playerOrderDestinationPitchArea;
    PlayerReceptionPreference receptionPreference;

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role, GameState state) {
        List<PlayerSkill> requiredSkills = duelType.requiredSkills(role, state.getBallState().getHeight());
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

                // TODO this logic should not be hardcoded. design needed.
                if (duelType.equals(DuelType.HEADER_SHOT)) {
                    int aerialSkill = skills.get(PlayerSkill.AERIAL);
                    if (skillValue > aerialSkill + 10) skillValue = aerialSkill + 10;
                }

                // TODO this logic should not be hardcoded. design needed.
                if (duelType.equals(DuelType.LONG_SHOT)) {
                    int passingSkill = skills.get(PlayerSkill.PASSING);
                    if (skillValue > passingSkill + 10) skillValue = passingSkill + 10;
                }

            }
        } else {
            // A value may not be present if the player is a goalkeeper, since goalkeepers have a
            // different set of skills.
            // TOD - refactor when implementing goalkeeper skills properly
            if (duelType.isPassing() && role == DuelRole.INITIATOR) {
                // Here the goalkeeper is attempting to pass the ball, but does not have the passing
                // skill. In this case, use the `organization` skill.
                skillValue = skills.get(PlayerSkill.ORGANIZATION);
            } else if (duelType.isShot() && role == DuelRole.CHALLENGER) {
                // Here the goalkeeper is attempting to save a shot - this requires the reflexes`
                // skill.
                Integer goalkeepingSkill = 0;
                switch (duelType) {
                    case LOW_SHOT, LONG_SHOT:
                        goalkeepingSkill = skills.get(PlayerSkill.REFLEXES);
                        break;
                    case ONE_TO_ONE_SHOT:
                        goalkeepingSkill = skills.get(PlayerSkill.ONE_ON_ONE);

                        String detail = "Goalkeeper used it's one to one skills: " + goalkeepingSkill + " instead of reflexes: " + skills.get(PlayerSkill.REFLEXES);
                        state.getRecorder().record(detail, state, GameProgressRecord.Type.ENTITY_BEHAVIOUR, GameProgressRecord.DuelStage.DURING);

                        break;
                    case HEADER_SHOT:
                        goalkeepingSkill = skills.get(PlayerSkill.REFLEXES);
                        break;
                }

                // TODO refactor this
                // Goalkeeper Positioning affects all types of shot.
                Integer diceRoll = (int) (Math.random() * 100);
                Integer goalkeepingPositioningSkill = skills.get(PlayerSkill.GOALKEEPER_POSITIONING);
                Integer goalkeepingPositioningModifier = goalkeepingPositioningSkill - diceRoll;

                StringBuilder detail = new StringBuilder("Applying goalkeeper positioning: ")
                        .append("Goalkeeper skill (RE/OTO): ").append(goalkeepingSkill)
                        .append(", Goalkeeper positioning: ").append(goalkeepingPositioningSkill)
                        .append(", Dice roll: ").append(diceRoll)
                        .append(", Goalkeeper skill (RE/OTO) modified with the addition of: ").append(goalkeepingPositioningModifier);
                state.getRecorder().record(detail.toString(), state, GameProgressRecord.Type.CALCULATION, GameProgressRecord.DuelStage.DURING);

                goalkeepingSkill += goalkeepingPositioningModifier;

                skillValue = goalkeepingSkill;;

            }
        }

        if (skillValue == null) throw new RuntimeException("Player " + this.getName() + " does not have any of the required skills: " + requiredSkills);

        return skillValue;
    }

    public Integer getSkillValue(PlayerSkill skill) {
        return this.getSkills().get(skill);
    }

    public void setSkillValue(PlayerSkill skill, Integer value) { this.getSkills().put(skill, value); }

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
