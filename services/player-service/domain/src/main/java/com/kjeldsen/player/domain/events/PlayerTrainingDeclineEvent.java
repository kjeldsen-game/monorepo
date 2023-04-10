package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.Range;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Document(collection = "PlayerTrainingDeclineEvents")
@TypeAlias("PlayerTrainingDeclineEvent")
public class PlayerTrainingDeclineEvent extends Event {

    private PlayerId playerId;
    private Integer declineStartAge;
    private Integer declineSpeed;
    private PlayerSkill skill;
    private Integer pointsToSubtract;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;
    private Integer currentDay;

    private static final Integer MIN_DECLINE_AGE = 15;
    private static final Integer MAX_DECLINE_AGE = 33;
    private static final Range<Integer> RANGE_OF_DECLINE_AGE = Range.between(MIN_DECLINE_AGE, MAX_DECLINE_AGE);
    private static final Integer MIN_SPEED = 0;
    private static final Integer MAX_SPEED = 100;
    private static final Range<Integer> RANGE_OF_SPEED = Range.between(MIN_SPEED, MAX_SPEED);

    public static PlayerTrainingDeclineEvent of(Integer declineStartAge, Integer declineSpeed, PlayerId playerId, PlayerSkill skill, Integer pointsToSubtract, Integer pointsBeforeTraining, Integer pointsAfterTraining, Integer currentDay) {


        if (!RANGE_OF_DECLINE_AGE.contains(declineStartAge)) {
            throw new IllegalArgumentException("Decline start age must be between 15 and 33");
        }

        if (!RANGE_OF_SPEED.contains(declineSpeed)) {
            throw new IllegalArgumentException("Decline speed must be between 0-1000");
        }

        return PlayerTrainingDeclineEvent.builder()
            .playerId(playerId)
            .declineStartAge(declineStartAge)
            .declineSpeed(declineSpeed)
            .skill(skill)
            .pointsToSubtract(pointsToSubtract)
            .pointsBeforeTraining(pointsBeforeTraining)
            .pointsAfterTraining(pointsAfterTraining)
            .currentDay(currentDay)
            .build();

    }


}
