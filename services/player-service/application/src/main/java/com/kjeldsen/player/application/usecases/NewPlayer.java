package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewPlayer {
    private PlayerAge age;
    private PlayerPosition position;
    private int points;
}
