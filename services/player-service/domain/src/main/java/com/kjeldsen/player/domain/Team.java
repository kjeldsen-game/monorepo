package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Team {
    private TeamId id;
    private TeamName name;
    private List<Player> players;
}
