package com.kjeldsen.player;

import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

@RequiredArgsConstructor
public enum PlayerPosition {
    FORWARD(EnumSet.of(PlayerAbility.SPEED.with(60, 50))),
    GOALKEEPER(EnumSet.of(PlayerAbility.SPEED.with(60, 50)));

    private final EnumSet<PlayerAbility> abilityReference;
}
