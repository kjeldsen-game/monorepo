package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

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
}
