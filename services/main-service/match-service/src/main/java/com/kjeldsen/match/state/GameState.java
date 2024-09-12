package com.kjeldsen.match.state;

import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.Match;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    // The clock is the number of minutes that have elapsed since the start of the game.
    int clock;

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
            .turn(Turn.HOME) // TODO randomize - this is temporarily set to home for testing
            .clock(0)
            .home(TeamState.init(match.getHome()))
            .away(TeamState.init(match.getAway()))
            .ballState(BallState.init())
            .plays(List.of())
            .build();
    }

    public TeamState attackingTeam() {
        return turn == Turn.HOME ? home : away;
    }

    public TeamState defendingTeam() {
        return turn == Turn.HOME ? away : home;
    }

    public Optional<Play> lastPlay() {
        if (plays.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(plays.get(plays.size() - 1));
    }

    // Since the game state is immutable, we need to create new lists when adding a play to the list
    // of plays. This is an efficient way of doing that, and performs better than the Stream API.
    public static List<Play> concatPlay(List<Play> plays, Play play) {
        List<Play> update = new ArrayList<>(plays.size() + 1);
        update.addAll(plays);
        update.add(play);
        return update;
    }

    // Returns a list of legal actions, which codify the rules of football. These are independent of
    // strategies and modifiers and are based solely on what a player can do in a given state.
    public List<Action> legalActions() {
        return lastPlay()
            .map(Play::getDuel)
            .map(lastDuel -> {
                // After a goal the only valid action is a goal-kick (treated as a pass)
                if (lastDuel.getType() == DuelType.LOW_SHOT) {
                    return List.of(Action.PASS);
                }
                // For all other cases we consult the duel type to determine what is allowed next.
                // There should not be many exceptions to this.
                return switch (lastDuel.getResult()) {
                    case WIN -> lastDuel.getType().winActions();
                    case LOSE -> lastDuel.getType().loseActions();
                };
            })
            .orElseGet(() -> {
                // At kickoff (no plays made) the only valid action is to pass the ball
                return List.of(Action.PASS);
            });
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

            TeamState activeTeam = this.turn == Turn.HOME ? home : away;
            activeTeam.getPlayers().stream()
                .filter((player) -> player.getId().equals(ballState.getPlayer().getId()))
                .findAny()
                .orElseThrow(
                    () -> new GameStateException("Player with ball is not on active team"));

            return this;
        }
    }
}
