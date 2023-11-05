package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.RandomHelper.genPlayers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.duel.Duel;
import com.kjeldsen.match.engine.state.BallState;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameState.Turn;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ActionSelectionTest {

    List<Action> allActions = Arrays.stream(Action.values()).toList();

    @Test
    void forwardInPenaltyBoxDoesNotPass() {
        Player forward = Player.builder().position(PlayerPosition.FORWARD).build();
        GameState state = setGameState(forward, PitchArea.CENTER_FORWARD);
        List<Action> actions = ActionSelection.filterActions(allActions, state, forward);
        assertFalse(actions.contains(Action.PASS));
    }

    @Test
    void midfielderDoesNotShoot() {
        Player midfielder = Player.builder().position(PlayerPosition.OFFENSIVE_MIDFIELDER).build();
        GameState state = setGameState(midfielder, PitchArea.RIGHT_FORWARD);
        List<Action> actions = ActionSelection.filterActions(allActions, state, midfielder);
        assertFalse(actions.contains(Action.SHOOT));
    }

    @Test
    void defenderPassesOnly() {
        Player defender = Player.builder().position(PlayerPosition.CENTER_BACK).build();
        GameState state = setGameState(defender, PitchArea.CENTER_BACK);
        List<Action> actions = ActionSelection.filterActions(allActions, state, defender);
        assertEquals(1, actions.size());
        assertEquals(Action.PASS, actions.get(0));
    }

    @Test
    void forwardPassesAndShootsInForwardFlanks() {
        Player forward = Player.builder().position(PlayerPosition.FORWARD).build();
        GameState state = setGameState(forward, PitchArea.LEFT_FORWARD);
        List<Action> actions = ActionSelection.filterActions(allActions, state, forward);
        assertTrue(actions.contains(Action.SHOOT));
        assertTrue(actions.contains(Action.POSITION));
    }

    // Helper to create a game state with a player in control of the ball in a particular area
    private GameState setGameState(Player player, PitchArea area) {
        player.setId(new Random().nextLong());
        Team home = Team.builder()
            .id(new Random().nextLong())
            .build();
        List<Player> players = genPlayers(home);
        players.remove(1);
        players.add(player);
        home = Team.builder()
            .id(home.getId())
            .players(players)
            .rating(1)
            .build();

        Play play = Play.builder()
            .duel(Duel.builder().build())
            .action(Action.PASS)
            .build();

        Team away = RandomHelper.genTeam();
        BallState ballState = new BallState(player, area);
        GameState state = GameState.init(Match.builder().home(home).away(away).build());
        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(Turn.HOME)
                    .clock(1)
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(ballState)
                    .plays(List.of(play))
                    .build())
            .orElseThrow();
    }
}
