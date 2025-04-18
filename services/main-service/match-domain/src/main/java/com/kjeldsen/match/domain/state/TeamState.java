package com.kjeldsen.match.domain.state;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.entities.stats.MatchStats;
import com.kjeldsen.match.domain.entities.stats.PlayerStats;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class TeamState {

    /*
     * Represents positions and conditions of players on the team
     */

    String id;
    TeamRole role;
    List<Player> players;
    List<Player> bench;
    MatchStats matchStats;

    int score; // TODO remove will be in matchStats

    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    public static TeamState init(Team team) {
        return TeamState.builder()
            .id(team.getId())
            .role(team.getRole())
            .players(team.getPlayers())
            .bench(team.getBench())
            .matchStats(MatchStats.init(team.getPlayers()))
            .score(0)
            .tactic(team.getTactic())
            .verticalPressure(team.getVerticalPressure())
            .horizontalPressure(team.getHorizontalPressure())
            .build();
    }

    public static TeamState copyTeamStateAndUpdate(TeamState teamState, MatchStats matchStats, boolean isGoal) {
        return TeamState.builder()
            .players(teamState.getPlayers())
            .bench(teamState.getBench())
            .tactic(teamState.getTactic())
            .verticalPressure(teamState.getVerticalPressure())
            .horizontalPressure(teamState.getHorizontalPressure())
            .matchStats(matchStats)
            .score(isGoal ? teamState.getScore() + 1 : teamState.getScore())
            .build();
    }
}
