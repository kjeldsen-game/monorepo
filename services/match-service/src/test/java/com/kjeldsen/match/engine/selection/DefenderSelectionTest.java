package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.RandomHelper.genPlayerWithPosition;
import static com.kjeldsen.match.engine.selection.DefenderSelection.selectFromMidfield;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.state.BallState;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
            genPlayerWithPosition(team, PlayerPosition.CENTER_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.OFFENSIVE_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.DEFENSIVE_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.LEFT_WINGER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.CENTER_MIDFIELDER, 0);
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
            new BallState(home.getPlayers().get(0), PitchArea.CENTER_MIDFIELD);

        GameState state = GameState.builder()
            .turn(GameState.Turn.HOME)
            .clock(0)
            .home(TeamState.init(match.getHome()))
            .away(TeamState.init(match.getAway()))
            .ballState(ballState)
            .plays(List.of())
            .build();

        // Make this number larger to get more accurate results
        for (int i = 0; i < 10000; i++) {
            Player selected = selectFromMidfield(state, PitchArea.CENTER_MIDFIELD);
            selections.merge(selected.getPosition(), 1, Integer::sum);
        }

        System.out.println(selections);
    }
}
