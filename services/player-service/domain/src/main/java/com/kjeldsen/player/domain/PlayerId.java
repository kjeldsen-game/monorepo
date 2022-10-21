package com.kjeldsen.player.domain;

import java.util.UUID;

public record PlayerId(String value) {
    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID().toString());
    }

    public static PlayerId of(String value) {
        return new PlayerId(value);
    }
}
