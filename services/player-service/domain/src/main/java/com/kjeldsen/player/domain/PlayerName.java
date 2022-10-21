package com.kjeldsen.player.domain;

import com.github.javafaker.Faker;

public record PlayerName(String value) {

    public static PlayerName generate() {
        Faker faker = new Faker();
        return new PlayerName(faker.name().fullName());
    }
}
