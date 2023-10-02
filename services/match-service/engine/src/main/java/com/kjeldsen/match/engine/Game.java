package com.kjeldsen.match.engine;

import static com.kjeldsen.match.engine.state.GameState.Turn.AWAY;
import static com.kjeldsen.match.engine.state.GameState.Turn.HOME;
import static com.kjeldsen.match.engine.state.GameState.concatPlay;

import com.kjeldsen.match.domain.aggregate.Duel;
import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.aggregate.Play;
import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.type.Action;
import com.kjeldsen.match.domain.type.DuelResult;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PitchArea;
import com.kjeldsen.match.engine.execution.DuelExecution;
import com.kjeldsen.match.engine.selection.ActionSelection;
import com.kjeldsen.match.engine.selection.PlayerSelection;
import com.kjeldsen.match.engine.selection.ReceiverSelection;
import com.kjeldsen.match.engine.state.BallState;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import java.util.List;
import java.util.Optional;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class Game {

    /*
     * Entry point to the engine. Here a match is converted into a game state and a succession of
     * plays are executed until the game is over.
     */

    public static int HALF_TIME = 45;
    public static int FULL_TIME = 90;

    // This method simulates a match. Any information required for the game to be executed needs to
    // be passed to this method as part of the match. It returns the final game state with a list
    // of every play that was executed during the match.
    public static GameState play(Match match) {
        // The game state is initialised to a pre-kick-off state based on the team data.
        // At this state no player is in possession of the ball but a team has been randomly
        // selected to start the game.
        log.info("Initialising game state for match {}", match.getMatchId());
        GameState state = GameState.init(match);

        log.info("Home team players");
        state.getHome().getPlayers().forEach(p -> log.info("{} {}", p.getPosition(), p.getName()));

        log.info("Away team players");
        state.getAway().getPlayers().forEach(p -> log.info("{} {}", p.getPosition(), p.getName()));

        log.info("Kicking off game...");
        state = kickOff(state);

        log.info("First half starting...");
        while (state.getClock() < HALF_TIME + state.getAddedTime()) {
            state = nextPlay(state);
        }

        log.info("Half time. Result: {}",
            state.getHome().getScore() + " - " + state.getAway().getScore());

        // Here we should switch sides and give the ball to the other team but for this version we
        // ignore first/second half and just carry on playing.

        log.info("Second half starting...");
        while (state.getClock() < (FULL_TIME + state.getAddedTime())) {
            state = nextPlay(state);
        }

        log.info("Match ended. Result: {}",
            state.getHome().getScore() + " - " + state.getAway().getScore());

        return state;
    }

    // At kick-off the game is set up with a player in possession of the ball. Any other pre-game
    // modification can be done here.
    public static GameState kickOff(GameState state) {
        Player starting = PlayerSelection.selectKickOffPlayer(state.getAttackingTeam());

        log.info("Starting player is {}", starting.getName());

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn())
                    .clock(before.getClock() + Action.PASS.getDuration())
                    .addedTime(before.getAddedTime())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState((new BallState(starting, PitchArea.MIDFIELD)))
                    .plays(before.getPlays())
                    .build())
            .orElse(state);
    }

    // This method is used to transition the game from play to play. Other than the kick-off method,
    // this is the only other method that should be called from the entrypoint method.
    public static GameState nextPlay(GameState state) {
        // At the start of each play a player must have the ball. As long as kick-off is called
        // before the first play this should always be the case.
        Player initiator = state.getBallState().getPlayer();

        // To decide on the next action, first get all valid actions for the player in possession of
        // the ball (these depend on the previous play and the result of play's duel) then delegate
        // the selection of one of them to the selection class.
        List<Action> actions = state.getValidActions();
        Action action = ActionSelection.selectAction(state, actions);

        // Generate a duel involving two players based on the action of the play. This requires
        // a challenger - the person to defend against the action by engaging in the duel - and
        // for some actions (e.g. pass) also a receiver. Player selection is also delegated.
        DuelType duelType = action.getDuelType();
        Player challenger = PlayerSelection.selectChallenger(state, duelType);
        // For duels that require a receiver always wrap this variable with an Optional and check
        // that an actual player is present
        Player receiver = null;
        if (duelType.requiresReceiver()) {
            receiver = ReceiverSelection.selectReceiver(state, initiator);
        }

        // The duel can be created once its result is determined. Provisionally, any modifiers
        // should be passed to the duel execution class to influence the result
        DuelResult result = DuelExecution.executeDuel(
            state, duelType, initiator, challenger, receiver);
        Duel duel = Duel.builder()
            .type(duelType)
            .initiator(initiator)
            .receiver(receiver)
            .challenger(challenger)
            .result(result)
            .pitchArea(state.getBallState().getArea())
            .build();

        // The play is now over and can be saved along with all its data. If further information
        // is needed for state transition it can be added to the play as metadata, for example.
        Play play = Play.builder()
            .duel(duel)
            .action(action)
            .minute(state.getClock())
            .build();

        // Once the play is complete we can update the game state based on it. All the information
        // required should be present in the play.
        if (result.equals(DuelResult.WIN)) {
            if (action.equals(Action.SHOOT)) {
                return handleGoal(state, play);
            } else {
                return handleDuelWin(state, play);
            }
        } else {
            return handleDuelLoss(state, play);
        }
    }

    private static GameState handleDuelWin(GameState state, Play play) {
        // For duel wins, we determine where the ball is (depending on whether there was a receiver)
        // then update clock and continue with the opportunity
        Duel duel = play.getDuel();
        final BallState newBallState;
        if (duel.getReceiver() != null) {
            newBallState = new BallState(duel.getReceiver(), duel.getPitchArea());
        } else {
            newBallState = new BallState(duel.getInitiator(), duel.getPitchArea());
        }

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn()) // Keep same team on turn
                    .clock(before.getClock() + play.getAction().getDuration())
                    .addedTime(before.getAddedTime())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(concatPlay(before.getPlays(), play))
                    .build())
            .orElse(state);
    }

    private static GameState handleDuelLoss(GameState state, Play play) {
        // For duel losses, we switch sides and give the ball to the challenger
        BallState newBallState =
            new BallState(play.getDuel().getChallenger(), play.getDuel().getPitchArea());
        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn() == HOME ? AWAY : HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    .addedTime(before.getAddedTime())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(concatPlay(before.getPlays(), play))
                    .build())
            .orElse(state);
    }

    private static GameState handleGoal(GameState state, Play play) {
        // For goals, we switch sides and increment the score
        TeamState newTeamState = Optional.of(state.getAttackingTeam())
            .map((before) ->
                TeamState.builder()
                    .players(before.getPlayers())
                    .playerLocation(before.getPlayerLocation())
                    .penaltyCards(before.getPenaltyCards())
                    .substitutions(before.getSubstitutions())
                    .score(before.getScore() + 1)
                    .fouls(before.getFouls())
                    .injuries(before.getInjuries())
                    .build())
            .orElseThrow();

        // Give the ball to the kick-off player from the team that conceded the goal
        BallState newBallState = new BallState(
            PlayerSelection.selectKickOffPlayer(state.getDefendingTeam()),
            play.getDuel().getPitchArea());

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn().equals(HOME) ? AWAY : HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    .addedTime(before.getAddedTime())
                    // Here we update the team whose turn it was since that's the team that scored
                    .home(before.getTurn() == HOME ? newTeamState : before.getHome())
                    .away(before.getTurn() == AWAY ? newTeamState : before.getAway())
                    .ballState(newBallState)
                    .plays(concatPlay(before.getPlays(), play))
                    .build())
            .orElse(state);
    }
}
