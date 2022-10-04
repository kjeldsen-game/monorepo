package com.kjeldsen.player.rest.delegate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PlayerAbility {

    SPEED(50, 100),
    GOAL(50, 100);

    private Integer min;
    private Integer max;

    PlayerAbility with(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }


}
