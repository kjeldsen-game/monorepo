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
}
