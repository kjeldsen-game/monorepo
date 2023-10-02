package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.TeamId;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Team {

    TeamId id;

    List<Player> players;
    List<Player> bench;

    int rating;
}
