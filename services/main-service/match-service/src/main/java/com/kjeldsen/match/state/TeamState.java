package com.kjeldsen.match.state;

import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TeamState {

    /*
     * Represents positions and conditions of players on the team
     */

    String id;
    List<Player> players;
    List<Player> bench;
    int score;

    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    public static TeamState init(Team team) {
        return TeamState.builder()
            .id(team.getId())
            .players(team.getPlayers())
            .bench(team.getBench())
            .score(0)
            .tactic(team.getTactic())
            .verticalPressure(team.getVerticalPressure())
            .horizontalPressure(team.getHorizontalPressure())
            .build();
    }

}
