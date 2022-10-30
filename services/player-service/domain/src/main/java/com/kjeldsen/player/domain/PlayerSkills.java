package com.kjeldsen.player.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public record PlayerSkills(Map<PlayerSkill, Integer> values) {

    public static PlayerSkills of(PlayerPosition position) {
        //TODO: Apply specific abilities value of a position
        return new PlayerSkills(new HashMap<>(
                Map.of(PlayerSkill.SC, PlayerSkill.SC.getMin(),
                        PlayerSkill.BC, PlayerSkill.BC.getMin(),
                        PlayerSkill.PA, PlayerSkill.PA.getMin(),
                        PlayerSkill.CO, PlayerSkill.CO.getMin(),
                        PlayerSkill.TA, PlayerSkill.TA.getMin())));
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

        SC(50, 100, 1),
        OP(50, 100, 1),
        BC(50, 100, 1),
        PA(50, 100, 1),
        CO(50, 100, 1),
        TA(50, 100, 1),
        DP(50, 100, 1);

        private Integer min;
        private Integer max;
        private Integer tendency;

        PlayerSkill with(Integer min, Integer max) {
            this.min = min;
            this.max = max;
            return this;
        }

        PlayerSkill withTendency(Integer tendency) {
            this.tendency = tendency;
            return this;
        }
    }
}
