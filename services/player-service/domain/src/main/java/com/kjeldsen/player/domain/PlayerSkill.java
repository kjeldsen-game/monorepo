package com.kjeldsen.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PlayerSkill {

    SCORE(1),
    OFFENSIVE_POSITION(1),
    BALL_CONTROL(1),
    PASSING(1),
    CO(1),
    TACKLING(1),
    DEFENSE_POSITION(1);

    private Integer tendency;

    PlayerSkill withTendency(Integer tendency) {
        this.tendency = tendency;
        return this;
    }

    public static Integer totalTendency() {
        return Arrays.stream(values()).mapToInt(PlayerSkill::getTendency).sum();
    }
}
