package com.kjeldsen.player.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public record PlayerSkills(Map<PlayerSkill, Integer> values) {

    public static PlayerSkills of(PlayerPosition position) {
        //TODO: Apply specific abilities value of a position
        return new PlayerSkills(new HashMap<>(
                Map.of(PlayerSkill.SPEED, PlayerSkill.SPEED.getMin(),
                        PlayerSkill.PASSING, PlayerSkill.PASSING.getMin(),
                        PlayerSkill.SHOOTING, PlayerSkill.SHOOTING.getMin(),
                        PlayerSkill.TACKLING, PlayerSkill.TACKLING.getMin(),
                        PlayerSkill.HEADING, PlayerSkill.HEADING.getMin())));
    }

    public static PlayerSkills of(Map<PlayerSkill, Integer> values) {
        return new PlayerSkills(values);
    }

    public void addAbilityPoints(PlayerSkill skill, int points) {
        values.merge(skill, points, Integer::sum);
    }

    public int getAbilityPoints(PlayerSkill skill) {
        return values.get(skill);
    }

    @AllArgsConstructor
    @Getter
    public enum PlayerSkill {

        SPEED(50, 100),
        GOAL(50, 100),
        PASSING(50, 100),
        SHOOTING(50, 100),
        TACKLING(50, 100),
        HEADING(50, 100);

        private Integer min;
        private Integer max;

        PlayerSkill with(Integer min, Integer max) {
            this.min = min;
            this.max = max;
            return this;
        }
    }
}
