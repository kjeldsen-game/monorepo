package com.kjeldsen.player.domain;

public record TeamId(String value) {
    public static TeamId generate() {
        return new TeamId(java.util.UUID.randomUUID().toString());
    }

    public static TeamId of(String id) {
        return new TeamId(id);
    }
}
