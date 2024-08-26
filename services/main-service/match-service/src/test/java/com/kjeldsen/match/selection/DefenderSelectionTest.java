package com.kjeldsen.match.selection;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.state.BallHeight;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kjeldsen.match.engine.RandomHelper.genActivePlayerWithPosition;
import static com.kjeldsen.match.selection.DefenderSelection.selectFromMidfield;

public class DefenderSelectionTest {


    // This test recreates an example from the specification of player selection.
    // We select a player to defend against an attack in the midfield area.
    // The probabilities for the five players to be selected are:
    // Center midfielder = 25%
    // Offensive midfielder = 0%
    // Defensive midfielder = 50%
    // Right midfielder = 25%
    // Left winger = 0%
    // After running many simulations we should see these numbers approximately.
    @Test
    @Disabled
    void selectMidfieldReceiverDistribution() {
        Team team = RandomHelper.genTeam();

        List<Player> players = List.of(
            genActivePlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.OFFENSIVE_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.DEFENSIVE_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.LEFT_WINGER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.CENTRE_MIDFIELDER, 0);
        selections.put(PlayerPosition.OFFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.DEFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.RIGHT_MIDFIELDER, 0);
        selections.put(PlayerPosition.LEFT_WINGER, 0);

        Team home = Team.builder().players(players).build();
        Match match = Match.builder()
            .home(home)
            .away(RandomHelper.genTeam())
            .build();

        BallState ballState =
            new BallState(home.getPlayers().get(0), PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND);

        GameState state = GameState.builder()
            .turn(GameState.Turn.HOME)
            .clock(0)
            .home(TeamState.init(match.getHome()))
            .away(TeamState.init(match.getAway()))
            .ballState(ballState)
            .plays(List.of())
            .build();

        // Make this number larger to get more accurate results
        for (int i = 0; i < 10; i++) {
            Player selected = selectFromMidfield(state, PitchArea.CENTRE_MIDFIELD);
            selections.merge(selected.getPosition(), 1, Integer::sum);
        }

        System.out.println(selections);
    }
}
