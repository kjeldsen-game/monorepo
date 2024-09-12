package com.kjeldsen.match.selection;

import com.kjeldsen.match.common.RandomHelper;
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

import static com.kjeldsen.match.common.RandomHelper.genActivePlayerWithPosition;
import static com.kjeldsen.match.selection.ReceiverSelection.selectFromForward;
import static com.kjeldsen.match.selection.ReceiverSelection.selectFromMidfield;

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
        Player initiator = genActivePlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER);

        List<Player> players = List.of(
            genActivePlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.DEFENSIVE_MIDFIELDER),
            genActivePlayerWithPosition(team, PlayerPosition.RIGHT_WINGER),
            genActivePlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.LEFT_MIDFIELDER, 0);
        selections.put(PlayerPosition.DEFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.RIGHT_WINGER, 0);
        selections.put(PlayerPosition.CENTRE_MIDFIELDER, 0);

        Team home = Team.builder().players(players).tactic(team.getTactic()).build();
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
        Player initiator = genActivePlayerWithPosition(team, PlayerPosition.FORWARD);

        List<Player> players = List.of(
            genActivePlayerWithPosition(team, PlayerPosition.FORWARD),
            genActivePlayerWithPosition(team, PlayerPosition.RIGHT_WINGER),
            genActivePlayerWithPosition(team, PlayerPosition.OFFENSIVE_MIDFIELDER));

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
            new BallState(home.getPlayers().get(0), PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND);

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
