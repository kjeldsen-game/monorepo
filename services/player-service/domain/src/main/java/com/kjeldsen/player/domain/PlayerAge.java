package com.kjeldsen.player.domain;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;

public record PlayerAge(Integer value) {

    private static final Integer MIN_AGE = 15;
    private static final Integer MAX_AGE = 33;
    private static final Range<Integer> RANGE_OF_AGE = Range.between(MIN_AGE, MAX_AGE);

    public static PlayerAge generate() {
        return new PlayerAge(RandomUtils.nextInt(RANGE_OF_AGE.getMinimum(), RANGE_OF_AGE.getMaximum()));
    }

    public static PlayerAge of(int age) {

        if (!RANGE_OF_AGE.contains(age)) {
            throw new IllegalArgumentException("Age must be between 15 and 33");
        }

        return new PlayerAge(age);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
