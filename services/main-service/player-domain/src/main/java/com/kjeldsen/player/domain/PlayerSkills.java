package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.generator.PointsGenerator;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class PlayerSkills {
    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    private Integer actual;
    private Integer potential;
    private PlayerSkillRelevance playerSkillRelevance;

    public static PlayerSkills empty() {
        return PlayerSkills.builder().actual(0).potential(0).playerSkillRelevance(PlayerSkillRelevance.RESIDUAL).build();
    }

    public void increaseActualPoints(Integer points) {
        actual = Math.min(MAX_SKILL_VALUE, actual + points);
    }

    public void initializePotentialPoints() {
        potential = PointsGenerator.generatePotentialPoints(actual);
    }

    public static PlayerSkillRelevance getSkillRelevanceBasedOnPositionAndSkill(PlayerPosition position, PlayerSkill skill){
        switch (position){
            case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK:
                switch (skill){
                    case TACKLING, DEFENSIVE_POSITIONING: return PlayerSkillRelevance.CORE;
                    case AERIAL, CONSTITUTION: return PlayerSkillRelevance.SECONDARY;
                    case SCORING, OFFENSIVE_POSITIONING, BALL_CONTROL, PASSING: return PlayerSkillRelevance.RESIDUAL;
                    default: throw new IllegalStateException("Unexpected value: " + skill);
                }
            case LEFT_WINGBACK, RIGHT_WINGBACK, DEFENSIVE_MIDFIELDER:
                switch (skill){
                    case TACKLING, DEFENSIVE_POSITIONING: return PlayerSkillRelevance.CORE;
                    case AERIAL, CONSTITUTION: return PlayerSkillRelevance.SECONDARY;
                    case SCORING, OFFENSIVE_POSITIONING, BALL_CONTROL, PASSING: return PlayerSkillRelevance.RESIDUAL;
                    default: throw new IllegalStateException("Unexpected value: " + skill);
                }
            case LEFT_MIDFIELDER, CENTRE_MIDFIELDER, RIGHT_MIDFIELDER, LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER:
                switch (skill){
                    case PASSING: return PlayerSkillRelevance.CORE;
                    case OFFENSIVE_POSITIONING, BALL_CONTROL, CONSTITUTION, TACKLING, DEFENSIVE_POSITIONING: return PlayerSkillRelevance.SECONDARY;
                    case SCORING, AERIAL: return PlayerSkillRelevance.RESIDUAL;
                    default: throw new IllegalStateException("Unexpected value: " + skill);
                }
            case FORWARD, AERIAL_FORWARD, STRIKER, AERIAL_STRIKER:
                switch (skill){
                    case SCORING, OFFENSIVE_POSITIONING, BALL_CONTROL: return PlayerSkillRelevance.CORE;
                    case PASSING, AERIAL, CONSTITUTION: return PlayerSkillRelevance.SECONDARY;
                    case TACKLING, DEFENSIVE_POSITIONING: return PlayerSkillRelevance.RESIDUAL;
                    default: throw new IllegalStateException("Unexpected value: " + skill);
                }
            case GOALKEEPER:
                switch (skill){
                    case REFLEXES, GOALKEEPER_POSITIONING: return PlayerSkillRelevance.CORE;
                    case INTERCEPTIONS, ONE_ON_ONE: return PlayerSkillRelevance.SECONDARY;
                    case CONTROL, ORGANIZATION: return PlayerSkillRelevance.RESIDUAL;
                    default: throw new IllegalStateException("Unexpected value: " + skill);
                }
        }
        throw new IllegalStateException("Unexpected value: " + position);
    }
}
