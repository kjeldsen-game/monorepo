package com.kjeldsen.player.rest.delegate;

import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

@RequiredArgsConstructor
public enum PlayerPosition {
    FORWARD(EnumSet.of(PlayerAbility.SPEED.with(60, 50))),
    GOALKEEPER

    private final EnumSet<PlayerAbility> abilityReference;
    }
