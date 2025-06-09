package com.kjeldsen.match.domain.state;

import lombok.Getter;

@Getter
public enum ChainActionSequence {
    NONE,
    WALL_PASS,
    COUNTER_ATTACK,
    MISSED_PASS;

    public boolean isActive() {
        return !NONE.equals(this);
    }
}
