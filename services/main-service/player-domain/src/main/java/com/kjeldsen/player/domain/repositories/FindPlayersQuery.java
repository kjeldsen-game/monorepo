package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Team.TeamId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPlayersQuery {
    private TeamId teamId;
    private PlayerPosition position;
    private int size;
    private int page;
}
