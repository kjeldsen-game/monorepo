package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;

@Builder
@Data
public class PlayerDeclineEvent {

    private PlayerId playerId;
    private int yearsOn;
    private int declineStartAge;
    private int declineSpeed;

    private static final Integer MIN_DECLINE_AGE = 15;
    private static final Integer MAX_DECLINE_AGE = 30;
    private static final Range<Integer> RANGE_OF_DECLINE_AGE = Range.between(MIN_DECLINE_AGE, MAX_DECLINE_AGE);

    public static PlayerDeclineEvent of(int declineYearsOn) {

        if (!RANGE_OF_DECLINE_AGE.contains(declineYearsOn)) {
            throw new IllegalArgumentException("Decline years on must be between 0 and 10");
        }

        return PlayerDeclineEvent.builder()
            .yearsOn(declineYearsOn)
            .build();
    }

    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 10000;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

}
