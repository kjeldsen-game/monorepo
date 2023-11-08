package com.kjeldsen.player.domain.repositories;

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
