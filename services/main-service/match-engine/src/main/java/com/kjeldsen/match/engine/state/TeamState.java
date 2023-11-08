package com.kjeldsen.match.engine.state;

import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TeamState {

    /*
     * Represents positions and conditions of players on the team
     */

    Long id; // Temp - should not be here
    List<Player> players;
    int score;

    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    public static TeamState init(Team team) {
        return TeamState.builder()
            .id(team.getId())
            .players(team.getPlayers())
            .score(0)
            .tactic(team.getTactic())
            .verticalPressure(team.getVerticalPressure())
            .horizontalPressure(team.getHorizontalPressure())
            .build();
    }
}
