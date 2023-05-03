package com.kjeldsen.player.domain.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomGenerator {

    public static double random() {
        return Math.random();
    }

}
