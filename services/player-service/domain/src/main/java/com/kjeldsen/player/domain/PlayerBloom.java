package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;

@Builder
@Data
public class PlayerBloom {
    private boolean bloom;
    private int bloomYearsOn;
    private int bloomStartAge;
    private int bloomSpeedIncreaser;

    private static final Integer MIN_BLOOM_AGE = 15;
    private static final Integer MAX_BLOOM_AGE = 30;
    private static final Range<Integer> RANGE_OF_BLOOM_AGE = Range.between(MIN_BLOOM_AGE, MAX_BLOOM_AGE);

    public static PlayerBloom of(int bloomYearsOn) {

        if (!RANGE_OF_BLOOM_AGE.contains(bloomYearsOn)) {
            throw new IllegalArgumentException("Bloom years on must be between 0 and 10");
        }

        return PlayerBloom.builder()
            .bloomYearsOn(bloomYearsOn)
            .build();
    }

    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 10000;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

}
