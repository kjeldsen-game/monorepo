package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Data
@Document(collection = "PlayerTrainingDeclineEvents")
@TypeAlias("PlayerTrainingDeclineEvent")
public class PlayerTrainingDeclineEvent extends Event {

    private PlayerId playerId;
    private int yearsOn;
    private int declineStartAge;
    private int declineSpeed;

    private static final Integer MIN_DECLINE_AGE = 15;
    private static final Integer MAX_DECLINE_AGE = 33;
    private static final Range<Integer> RANGE_OF_DECLINE_AGE = Range.between(MIN_DECLINE_AGE, MAX_DECLINE_AGE);

    private static final Integer MIN_DECLINE_PHASE_ON = 1;
    private static final Integer MAX_DECLINE_PHASE_ON = 10;
    private static final Range<Integer> RANGE_OF_DECLINE_PHASE_ON = Range.between(MIN_DECLINE_PHASE_ON, MAX_DECLINE_PHASE_ON);

    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 100;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

    public static PlayerTrainingDeclineEvent of(int YearsOn, int declineStartAge, int declineSpeed, PlayerId playerId) {

        if (!RANGE_OF_DECLINE_PHASE_ON.contains(YearsOn)) {
            throw new IllegalArgumentException("Decline years on must be between 0 and 10");
        }

        if (!RANGE_OF_DECLINE_AGE.contains(declineStartAge)) {
            throw new IllegalArgumentException("Decline start age must be between 15 and 33");
        }

        if (!RANGE_OF_SPEED.contains(declineSpeed)) {
            throw new IllegalArgumentException("Decline speed must be between 0-1000");
        }

        return PlayerTrainingDeclineEvent.builder()
            .playerId(playerId)
            .yearsOn(YearsOn)
            .declineStartAge(declineStartAge)
            .declineSpeed(declineSpeed)
            .build();
    }


}
