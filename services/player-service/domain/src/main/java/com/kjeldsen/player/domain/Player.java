package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.events.PlayerBloomEvent;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.Range;

@Data
@Builder
@ToString
public class Player {

    public static Player generate(PlayerPositionTendency positionTendencies, int totalPoints) {
        return Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(PlayerAge.generate())
            .position(positionTendencies.getPosition())
            .actualSkills(PlayerActualSkills.generate(positionTendencies, totalPoints))
            .build();
    }

    private PlayerId id;
    private PlayerName name;
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerActualSkills actualSkills;

    public boolean isBloomActive(PlayerBloomEvent playerBloomEvent) {
        int initialRange = playerBloomEvent.getBloomStartAge();
        int endRange = initialRange + playerBloomEvent.getYearsOn();
        return Range.between(initialRange, endRange).contains(age.value());
    }

}
