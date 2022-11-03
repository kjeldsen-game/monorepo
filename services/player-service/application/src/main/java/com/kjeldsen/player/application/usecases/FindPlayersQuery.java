package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerPosition;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPlayersQuery {
    private PlayerPosition position;
    private int size;
    private int page;
}
