package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.Event;
import com.kjeldsen.player.domain.PlayerId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collection = "PlayerTrainingBloomEvents")
@TypeAlias("PlayerTrainingBloomEvent")
public class PlayerTrainingBloomEvent extends Event {

    private PlayerId playerId;
    private int yearsOn;
    private int bloomStartAge;
    private int bloomSpeed;

    private static final Integer MIN_BLOOM_AGE = 15;
    private static final Integer MAX_BLOOM_AGE = 30;
    private static final Range<Integer> RANGE_OF_BLOOM_AGE = Range.between(MIN_BLOOM_AGE, MAX_BLOOM_AGE);

    private static final Integer MIN_BLOOM_PHASE_ON = 1;
    private static final Integer MAX_BLOOM_PHASE_ON = 10;
    private static final Range<Integer> RANGE_OF_BLOOM_PHASE_ON = Range.between(MIN_BLOOM_PHASE_ON, MAX_BLOOM_PHASE_ON);

    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 1000;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

    public static PlayerTrainingBloomEvent of(int bloomYearsOn, int bloomStartAge, PlayerId playerId, int bloomSpeed) {

        if (!RANGE_OF_BLOOM_PHASE_ON.contains(bloomYearsOn)) {
            throw new IllegalArgumentException("Bloom years on must be between 0 and 10");
        }

        if (!RANGE_OF_BLOOM_AGE.contains(bloomStartAge)) {
            throw new IllegalArgumentException("Bloom start age must be between 15 and 30");
        }

        if (!RANGE_OF_SPEED.contains(bloomSpeed)) {
            throw new IllegalArgumentException("Bloom speed must be between 0-1000");
        }

        return PlayerTrainingBloomEvent.builder()
            .playerId(playerId)
            .yearsOn(bloomYearsOn)
            .bloomStartAge(bloomStartAge)
            .bloomSpeed(bloomSpeed)
            .build();
    }

}
