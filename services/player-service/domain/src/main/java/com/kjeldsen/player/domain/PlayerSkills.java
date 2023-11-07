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


    //TODO type de Mudo va aqui (por orden del maestro jedi https://trello.com/c/KiQgS1QA/110-skill-types)

    public void increaseActualPoints(Integer points) {
        actual = Math.min(MAX_SKILL_VALUE, actual + points);
    }

    public void initializePotentialPoints() {
        potential = PointsGenerator.generatePotentialPoints(actual);
    }

}
