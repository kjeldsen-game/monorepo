package com.kjeldsen.match.state;

import lombok.Getter;

@Getter
public enum ChainActionSequence {
    NONE,
    WALL_PASS;

    public boolean isActive() {
        return !NONE.equals(this);
    }

}
