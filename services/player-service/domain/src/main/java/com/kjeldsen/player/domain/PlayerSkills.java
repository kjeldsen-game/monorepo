package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.generator.PointsGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString

public class PlayerSkills {
    private Integer actual;
    private Integer potential;

    //TODO type de Mudo va aqui (por orden del maestro jedi https://trello.com/c/KiQgS1QA/110-skill-types)

    public void increaseActualPoints(Integer points) {
        actual += points;
    }

    public void initializePotentialPoints() {
        potential = PointsGenerator.generatePotencialPoints(actual);
    }

}
