package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.RandomHelper.genPlayerWithPosition;
import static com.kjeldsen.match.engine.selection.ReceiverSelection.selectFromForward;
import static com.kjeldsen.match.engine.selection.ReceiverSelection.selectFromMidfield;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.state.BallState;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.engine.entities.Match;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.Team;
import com.kjeldsen.match.engine.entities.Player;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ReceiverSelectionTest {

    // This test recreates an example from the specification of player selection.
    // We select a player to receive the ball from midfield. The probabilities for the four players
    // to receive the ball are roughly:
    // Left midfielder = 22%
    // Defensive midfielder = 11%
    // Right winger = 44%
    // Center midfielder = 22%
    // After running many simulations we should see these numbers approximately.
    @Test
    @Disabled
    void selectMidfieldReceiverDistribution() {
        Team team = RandomHelper.genTeam();
        Player initiator = genPlayerWithPosition(team, PlayerPosition.CENTER_MIDFIELDER);

        List<Player> players = List.of(
            genPlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.DEFENSIVE_MIDFIELDER),
            genPlayerWithPosition(team, PlayerPosition.RIGHT_WINGER),
            genPlayerWithPosition(team, PlayerPosition.CENTER_MIDFIELDER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.LEFT_MIDFIELDER, 0);
        selections.put(PlayerPosition.DEFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.RIGHT_WINGER, 0);
        selections.put(PlayerPosition.CENTER_MIDFIELDER, 0);

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
        for (int i = 0; i < 10; i++) {
            Player selected = selectFromMidfield(state, initiator);
            selections.merge(selected.getPosition(), 1, Integer::sum);
        }

        System.out.println(selections);
    }

    // A test similar to the one above but for a forward.
    // The probabilities for the three players are:
    // Forward = 50%
    // Right winger = 25%
    // Offensive midfielder = 25%
    @Test
    @Disabled
    void selectForwardReceiverDistribution() {
        Team team = RandomHelper.genTeam();
        Player initiator = genPlayerWithPosition(team, PlayerPosition.FORWARD);

        List<Player> players = List.of(
            genPlayerWithPosition(team, PlayerPosition.FORWARD),
            genPlayerWithPosition(team, PlayerPosition.RIGHT_WINGER),
            genPlayerWithPosition(team, PlayerPosition.OFFENSIVE_MIDFIELDER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.FORWARD, 0);
        selections.put(PlayerPosition.RIGHT_WINGER, 0);
        selections.put(PlayerPosition.OFFENSIVE_MIDFIELDER, 0);

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

        for (int i = 0; i < 10; i++) {
            Player selected = selectFromForward(state, initiator);
            selections.merge(selected.getPosition(), 1, Integer::sum);
        }

        System.out.println(selections);
    }

}
