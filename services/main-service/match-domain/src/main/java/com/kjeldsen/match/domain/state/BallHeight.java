package com.kjeldsen.match.domain.state;

import lombok.Getter;

import java.util.List;

@Getter
public enum BallHeight {
    GROUND,
    LOW,
    HIGH;

    public boolean isLow() {
        return List.of(BallHeight.GROUND, BallHeight.LOW).contains(this);
    }

    public boolean isHigh() {
        return List.of(BallHeight.HIGH).contains(this);
    }

}
