package com.kjeldsen.match.engine.state;

import com.kjeldsen.match.domain.aggregate.Duel;
import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.aggregate.Play;
import com.kjeldsen.match.domain.type.DuelResult;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PitchArea;
import com.kjeldsen.match.domain.type.Action;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameState {

    /*
     * The game state is the main data structure used by the engine to keep track of the game.
     * Each time a play is executed, the game state is updated and that play is added to the list
     * of plays. This allows us to replay the game later, or to analyse the plays, or to backtrack
     * and infer information based on the history of plays.
     */

    public enum Turn {
        HOME,
        AWAY
    }

    // The turn field is used to keep track of which team is currently in control of a play
    Turn turn;

    // The clock is the number of minutes that have elapsed since the start of the game. At 45
    // minutes the play ends and the teams switch sides. At 90 minutes the game ends.
    int clock;
    // Time added after fouls, injuries, etc.
    int addedTime;

    // State of other components in game
    TeamState home;
    TeamState away;
    BallState ballState;

    // The list of plays executed so far in the game
    List<Play> plays;

    // The initial game state simply selects a random team to start the game. Everything else is set
    // to a default start value.
    public static GameState init(Match match) {
        return GameState.builder()
            .turn(new Random().nextBoolean() ? Turn.HOME : Turn.AWAY)
            .clock(0)
            .addedTime(0)
            .home(TeamState.init(match.getHome()))
            .away(TeamState.init(match.getAway()))
            .ballState(BallState.init())
            .plays(List.of())
            .build();
    }

    public TeamState getAttackingTeam() {
        return turn == Turn.HOME ? home : away;
    }

    public TeamState getDefendingTeam() {
        return turn == Turn.HOME ? away : home;
    }

    // Since the game state is immutable, we need to create new lists when adding a play to the list
    // of plays. This is an efficient way of doing that, and performs better than the Stream API.
    public static List<Play> concatPlay(List<Play> plays, Play play) {
        List<Play> update = new ArrayList<>(plays.size() + 1);
        update.addAll(plays);
        update.add(play);
        return update;
    }

    // Returns a list of valid actions. These are independent of strategies and modifiers and are
    // based solely on what a player can legally do in a given state.
    public List<Action> getValidActions() {
        int size = plays.size();

        // At kickoff the only valid action is to pass the ball
        if (size == 0) {
            return List.of(Action.PASS);
        }

        Duel lastDuel = plays.get(plays.size() - 1).getDuel();

        // After a goal the only valid action is a goal-kick (treated as a pass)
        if (lastDuel.getType().equals(DuelType.SHOT)) {
            return List.of(Action.PASS);
        }

        // For all other cases we consult the duel type to determine what is allowed next. There
        // should not be many exceptions to this.
        if (lastDuel.getResult() == DuelResult.WIN) {
            return lastDuel.getType().getWinActions();
        } else {
            return lastDuel.getType().getLoseActions();
        }
    }

    @SuppressWarnings("unused")
    public static class GameStateBuilder {

        /*
         * Validation - the builder pattern is used throughout the code base to create new game
         * states so here builder methods are overridden to perform validation.
         */

        public GameStateBuilder ballState(BallState ballState) {
            this.ballState = ballState;

            // The only instance when the ball can exist without a player is before kick off,
            // which is inferred by the duration of the game being zero. At all other times
            // a player must be in possession of the ball.
            if (ballState.getPlayer() == null) {
                if (clock == 0) {
                    return this;
                } else {
                    throw new GameStateException("No player is in possession of the ball");
                }
            }

            // The player with the ball must be on the active team and must also be in the same
            // pitch area as the ball

            TeamState activeTeam = this.turn == Turn.HOME ? home : away;

            activeTeam.getPlayers().stream()
                .filter((player) -> player.getId().equals(ballState.getPlayer().getId()))
                .findAny()
                .orElseThrow(
                    () -> new GameStateException("Player with ball is not on active team"));

            PitchArea playerArea = activeTeam.getPlayerLocation()
                .get(ballState.getPlayer().getId());
            if (ballState.getArea() != null && ballState.getArea() != playerArea) {
                throw new GameStateException(
                    String.format(
                        "Player with ball is not in same area as ball (player in %s, ball in %s})",
                        playerArea,
                        ballState.getArea()));
            }

            return this;
        }
    }
}

