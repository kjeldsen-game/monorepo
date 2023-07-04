package com.kjeldsen.match.domain.processor;

import com.kjeldsen.match.domain.type.DuelType;

public class DuelStrategyFactory {
    public static DuelStrategy create(DuelType duelType) {
        return switch (duelType) {
            case PASS -> new PassDuelStrategy();
            case BALL_CONTROL -> new BallControlDuelStrategy();
        };
    }
}
