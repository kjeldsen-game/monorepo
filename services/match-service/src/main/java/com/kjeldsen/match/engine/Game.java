package com.kjeldsen.match.engine;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.duel.Duel;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.execution.DuelDTO;
import com.kjeldsen.match.engine.execution.DuelExecution;
import com.kjeldsen.match.engine.selection.ActionSelection;
import com.kjeldsen.match.engine.selection.ChallengerSelection;
import com.kjeldsen.match.engine.selection.KickOffSelection;
import com.kjeldsen.match.engine.selection.PitchAreaSelection;
import com.kjeldsen.match.engine.selection.ReceiverSelection;
import com.kjeldsen.match.engine.state.BallState;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameState.Turn;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.Player;
import java.util.Optional;
import java.util.Random;
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
        log.info("Initialising game state for match {}", match.getId());
        GameState state = GameState.init(match);

        log.info("Home team:\n{}", match.getHome());
        log.info("Away team:\n{}", match.getAway());

        state = kickOff(state);

        log.info("First half starting...");
        while (state.getClock() <= HALF_TIME) {
            state = nextPlay(state);
        }

        // Here we should switch sides and give the ball to the other team but for this version we
        // ignore first/second half and just carry on playing.
        log.info("Second half starting...");
        while (state.getClock() <= FULL_TIME) {
            state = nextPlay(state);
        }

        log.info(
            "Match ended. Result: {}",
            state.getHome().getScore() + " - " + state.getAway().getScore());
        return state;
    }

    // At kick-off the game is set up with a player in possession of the ball. Any other pre-game
    // modification can be done here.
    public static GameState kickOff(GameState state) {
        Player starting = KickOffSelection.selectPlayer(state, state.attackingTeam());

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn())
                    .clock(before.getClock() + Action.PASS.getDuration())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState((new BallState(starting, PitchArea.CENTER_MIDFIELD)))
                    .plays(before.getPlays())
                    .build())
            .orElseThrow();
    }

    // This method is used to transition the game from play to play. Other than the kick-off method,
    // this is the only other method that should be called from the entrypoint method.
    public static GameState nextPlay(GameState state) {
        // At the start of each play a player must have the ball. As long as kick-off is called
        // before the first play this should always be the case.
        Player initiator = state.getBallState().getPlayer();

        // Next an action is chosen for the player. Action selection happens in two stages: first
        // a list of legal moves is determined, then filters are applied based on rules given by
        // users (e.g. strategies) and by rules to improve gameplay.
        Action action = ActionSelection.selectAction(state, initiator);

        // Generate a duel based on the action of the play. This requires a challenger - the person
        // to defend against the action by engaging in the duel - and for some actions (e.g. pass)
        // also a receiver. Player selection is also delegated to the selection module.
        DuelType duelType = action.getDuelType();
        Player challenger = ChallengerSelection.selectChallenger(state, duelType);
        // For duels that require a receiver always wrap this variable with an Optional and check
        // that an actual player is present.
        Player receiver = null;
        if (duelType.requiresReceiver()) {
            receiver = ReceiverSelection.selectReceiver(state, initiator);
        }

        // The duel can be created once its result is determined. Any details about the duel that
        // need to be stored for future analysis should be set in the duel DTO and saved as part of
        // the duel here.
        DuelDTO outcome =
            DuelExecution.executeDuel(state, duelType, initiator, challenger, receiver);
        Duel duel = Duel.builder()
            .type(duelType)
            .initiator(initiator)
            .receiver(receiver)
            .challenger(challenger)
            .result(outcome.getResult())
            .pitchArea(state.getBallState().getArea())
            .initiatorStats(outcome.getInitiatorStats())
            .challengerStats(outcome.getChallengerStats())
            .build();

        // The play is now over. It is saved and the state is transitioned depending on the result.
        Play play = Play.builder()
            .id(new Random().nextLong())
            .duel(duel)
            .action(action)
            .minute(state.getClock())
            .build();

        log.info("Play complete:\n{}", play);

        if (outcome.getResult() == DuelResult.WIN) {
            if (action == Action.SHOOT) {
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
        // then increment clock and continue with the opportunity
        Duel duel = play.getDuel();
        BallState newBallState = Optional.ofNullable(duel.getReceiver())
            .map(receiver -> {
                PitchArea pitchArea =
                    PitchAreaSelection.select(state, duel.getReceiver(), DuelRole.INITIATOR);
                return new BallState(duel.getReceiver(), pitchArea);
            })
            .orElseGet(() -> {
                PitchArea pitchArea =
                    PitchAreaSelection.select(state, duel.getInitiator(), DuelRole.INITIATOR);
                return new BallState(duel.getInitiator(), pitchArea);
            });

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn()) // Keep same team on turn
                    .clock(before.getClock() + play.getAction().getDuration())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .build())
            .orElseThrow();
    }

    private static GameState handleDuelLoss(GameState state, Play play) {
        // For duel losses, we switch sides and give the ball to the challenger
        PitchArea pitchArea = PitchAreaSelection.select(
            state, play.getDuel().getChallenger(), DuelRole.CHALLENGER);
        BallState newBallState = new BallState(play.getDuel().getChallenger(), pitchArea);
        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn() == Turn.HOME ? Turn.AWAY : Turn.HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .build())
            .orElseThrow();
    }

    private static GameState handleGoal(GameState state, Play play) {
        // For goals, we switch sides and increment the score
        TeamState newTeamState = Optional.of(state.attackingTeam())
            .map((before) ->
                TeamState.builder()
                    .players(before.getPlayers())
                    .tactic(before.getTactic())
                    .verticalPressure(before.getVerticalPressure())
                    .horizontalPressure(before.getHorizontalPressure())
                    .score(before.getScore() + 1)
                    .build())
            .orElseThrow();

        // Give the ball to the kick-off player from the team that conceded the goal
        BallState newBallState = new BallState(
            KickOffSelection.selectPlayer(state, state.defendingTeam()),
            PitchArea.CENTER_MIDFIELD);

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn() == Turn.HOME ? Turn.AWAY : Turn.HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    // Here we update the team whose turn it was since that's the team that scored
                    .home(before.getTurn() == Turn.HOME ? newTeamState : before.getHome())
                    .away(before.getTurn() == Turn.AWAY ? newTeamState : before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .build())
            .orElseThrow();
    }
}
