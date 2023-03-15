package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;

@Builder
@Data
public class PlayerBloomEvent {

    private PlayerId playerId;
    private int yearsOn;
    private int bloomStartAge;
    private int bloomSpeedIncreaser;

    private static final Integer MIN_BLOOM_AGE = 15;
    private static final Integer MAX_BLOOM_AGE = 30;
    private static final Range<Integer> RANGE_OF_BLOOM_AGE = Range.between(MIN_BLOOM_AGE, MAX_BLOOM_AGE);

    public static PlayerBloomEvent of(int bloomYearsOn) {

        if (!RANGE_OF_BLOOM_AGE.contains(bloomYearsOn)) {
            throw new IllegalArgumentException("Bloom years on must be between 0 and 10");
        }

        return PlayerBloomEvent.builder()
            .yearsOn(bloomYearsOn)
            .build();
    }

    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 10000;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

}
