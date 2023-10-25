package com.kjeldsen.match.entities;

import com.kjeldsen.match.entities.player.Player;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Team {

    Id id;

    List<Player> players;
    List<Player> bench;

    int rating;
}
