package com.kjeldsen.player.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public record PlayerAbilities(Map<PlayerAbility, Integer> values) {

    public static PlayerAbilities of(PlayerPosition position) {
        //TODO: Apply specific abilities value of a position
        return new PlayerAbilities(new HashMap<>(
                Map.of(PlayerAbility.SPEED, PlayerAbility.SPEED.getMin(),
                        PlayerAbility.PASSING, PlayerAbility.PASSING.getMin(),
                        PlayerAbility.SHOOTING, PlayerAbility.SHOOTING.getMin(),
                        PlayerAbility.TACKLING, PlayerAbility.TACKLING.getMin(),
                        PlayerAbility.HEADING, PlayerAbility.HEADING.getMin())));
    }

    public void addAbilityPoints(PlayerAbility playerAbility, int points) {
        values.merge(playerAbility, points, Integer::sum);
    }

    public int getAbilityPoints(PlayerAbility playerAbility) {
        return values.get(playerAbility);
    }

    @AllArgsConstructor
    @Getter
    public enum PlayerAbility {

        SPEED(50, 100),
        GOAL(50, 100),
        PASSING(50, 100),
        SHOOTING(50, 100),
        TACKLING(50, 100),
        HEADING(50, 100);

        private Integer min;
        private Integer max;

        PlayerAbility with(Integer min, Integer max) {
            this.min = min;
            this.max = max;
            return this;
        }
    }
}
