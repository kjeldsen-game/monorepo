package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerTendency;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewPlayer {
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerTendency playerTendency;
    private int points;
}
