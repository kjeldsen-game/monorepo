package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewPlayer {
    private PlayerAge age;
    private PlayerPosition position;
    private int points;

    private TeamId teamId;
}
