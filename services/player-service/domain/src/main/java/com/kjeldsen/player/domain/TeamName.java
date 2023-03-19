package com.kjeldsen.player.domain;

public record TeamName(String value) {
    public static TeamName of(String value) {
        return new TeamName(value);
    }
}
