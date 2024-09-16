package com.kjeldsen.match.selection;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.BallHeight;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.GameState.Turn;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerReceptionPreference;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuelTypeSelectionTest {

    List<Action> allActions = Arrays.stream(Action.values()).toList();
    List<DuelType> allDuelTypes = Arrays.stream(DuelType.values()).toList();

    @Test
    void demandLowReceptionPreferenceReceivesLowPass() {
        Play passPlay = Play.builder().duel(Duel.builder().build()).action(Action.PASS).build();
        List<Play> previousPlays = List.of(passPlay);

        Player forward = Player.builder().position(PlayerPosition.FORWARD).receptionPreference(PlayerReceptionPreference.DEMAND_LOW).build();
        BallState ballState = new BallState(forward, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);
        GameState state = setGameState(previousPlays, forward, ballState);

        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, forward);
        assertEquals(DuelType.PASSING_LOW, duelType);
    }

    @Test
    void demandHighReceptionPreferenceReceivesHighPass() {
        Play passPlay = Play.builder().duel(Duel.builder().build()).action(Action.PASS).build();
        List<Play> previousPlays = List.of(passPlay);

        Player forward = Player.builder().position(PlayerPosition.FORWARD).receptionPreference(PlayerReceptionPreference.DEMAND_HIGH).build();
        BallState ballState = new BallState(forward, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);
        GameState state = setGameState(previousPlays, forward, ballState);

        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, forward);
        assertEquals(DuelType.PASSING_HIGH, duelType);
    }

    @Test
    void lowShotAfterBallControlWin() {

        Player midfielder = Player.builder().position(PlayerPosition.CENTRE_MIDFIELDER).build();
        BallState lastPlayBallState = new BallState(midfielder, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);

        Play passPlay = Play.builder().duel(Duel.builder().type(DuelType.PASSING_LOW).build())
                .ballState(lastPlayBallState).action(Action.PASS).build();
        Play positionalPlay = Play.builder().duel(Duel.builder().type(DuelType.POSITIONAL).build())
                .ballState(lastPlayBallState).action(Action.POSITION).build();
        Play tacklePlay = Play.builder().duel(Duel.builder().type(DuelType.BALL_CONTROL).build())
                .ballState(lastPlayBallState).action(Action.TACKLE).build();
        List<Play> previousPlays = List.of(passPlay, positionalPlay, tacklePlay);

        Player forward = Player.builder().position(PlayerPosition.FORWARD).build();
        BallState ballState = new BallState(forward, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);
        GameState state = setGameState(previousPlays, forward, ballState);

        DuelType duelType = DuelTypeSelection.select(state, Action.SHOOT, forward);
        assertEquals(DuelType.LOW_SHOT, duelType);
    }

    @Test
    void oneToOneShotAfterPositionalWin() {
        Player midfielder = Player.builder().position(PlayerPosition.CENTRE_MIDFIELDER).build();
        BallState lastPlayBallState = new BallState(midfielder, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);

        Play passPlay = Play.builder().duel(Duel.builder().type(DuelType.PASSING_LOW).build())
                .ballState(lastPlayBallState).action(Action.PASS).build();
        Play positionalPlay = Play.builder().duel(Duel.builder().type(DuelType.POSITIONAL).build())
                .ballState(lastPlayBallState).action(Action.POSITION).build();
        List<Play> previousPlays = List.of(passPlay, positionalPlay);

        Player forward = Player.builder().position(PlayerPosition.FORWARD).build();
        BallState ballState = new BallState(forward, PitchArea.CENTRE_FORWARD, BallHeight.GROUND);
        GameState state = setGameState(previousPlays, forward, ballState);

        DuelType duelType = DuelTypeSelection.select(state, Action.SHOOT, forward);
        assertEquals(DuelType.ONE_TO_ONE_SHOT, duelType);
    }

    @Test
    void headerShotAfterHighPassWin() {
        Player forward = Player.builder().position(PlayerPosition.FORWARD).build();
        BallState ballState = new BallState(forward, PitchArea.CENTRE_FORWARD, BallHeight.HIGH);

        Play passPlay = Play.builder().ballState(ballState).duel(Duel.builder().build()).action(Action.PASS).build();
        List<Play> previousPlays = List.of(passPlay);
        GameState state = setGameState(previousPlays, forward, ballState);

        DuelType duelType = DuelTypeSelection.select(state, Action.SHOOT, forward);
        assertEquals(DuelType.HEADER_SHOT, duelType);
    }

    // Helper to create a game state with a player in control of the ball in a particular area
    private GameState setGameState(List<Play> previousPlays, Player player, BallState ballState) {
        player.setId(RandomStringUtils.random(5));
        Team home = Team.builder()
            .id(RandomStringUtils.random(5))
            .build();
        List<Player> players = RandomHelper.genActivePlayers(home);
        List<Player> bench = RandomHelper.genBenchPlayers(home);
        players.remove(1);
        players.add(player);
        home = Team.builder()
            .id(home.getId())
            .players(players)
            .bench(bench)
            .rating(1)
            .build();

        Team away = RandomHelper.genTeam();
        GameState state = GameState.init(Match.builder().home(home).away(away).build());
        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(Turn.HOME)
                    .clock(1)
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(ballState)
                    .plays(previousPlays)
                    .build())
            .orElseThrow();
    }
}
