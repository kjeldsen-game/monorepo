package com.kjeldsen.match;

import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.entities.duel.DuelDisruptor;
import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.execution.DuelDTO;
import com.kjeldsen.match.execution.DuelExecution;
import com.kjeldsen.match.execution.DuelParams;
import com.kjeldsen.match.recorder.GameProgressRecord;
import com.kjeldsen.match.selection.ActionSelection;
import com.kjeldsen.match.selection.ChallengerSelection;
import com.kjeldsen.match.selection.DuelTypeSelection;
import com.kjeldsen.match.selection.KickOffSelection;
import com.kjeldsen.match.selection.PitchAreaSelection;
import com.kjeldsen.match.selection.ReceiverSelection;
import com.kjeldsen.match.state.BallHeight;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.GameState.Turn;
import com.kjeldsen.match.state.GameStateException;
import com.kjeldsen.match.state.TeamState;
import com.kjeldsen.match.validation.TeamFormationValidator;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerOrder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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

        GameState state = init(match);

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

    public static GameState init(Match match) {

        if (!TeamFormationValidator.validate(match.getHome()).getValid()) throw new RuntimeException("Home team formation is invalid.");
        if (!TeamFormationValidator.validate(match.getAway()).getValid()) throw new RuntimeException("Away team formation is invalid.");

        // The game state is initialised to a pre-kick-off state based on the team data.
        // At this state no player is in possession of the ball but a team has been randomly
        // selected to start the game.
        log.info("Initialising game state for match {}", match.getId());
        GameState state = GameState.init(match);

        //log.info("Home team:\n{}", match.getHome());
        //log.info("Away team:\n{}", match.getAway());

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
                    .ballState((new BallState(starting, PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND)))
                    .plays(before.getPlays())
                    .recorder(before.getRecorder())
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

        // For duels that require a receiver always wrap this variable with an Optional and check
        // that an actual player is present.
        // Also, define an intented pitch area to move the ball to in case of duel win. In this case, the pitch area should be nearby.
        Player receiver = null;
        PitchArea destinationPitchArea = null;
        if (action.requiresReceiver()) {
            receiver = ReceiverSelection.select(state, initiator);
            Boolean nearbyOnly = true;
            destinationPitchArea = PitchAreaSelection.select(state.getBallState().getArea(), receiver, true)
                    .orElseThrow(() -> new GameStateException(state, "No pitch area to select from"));
        }

        // Generate a duel based on the action of the play. This requires a challenger - the person
        // to defend against the action by engaging in the duel - and for some actions (e.g. pass)
        // also a receiver. Player selection is also delegated to the selection module.
        DuelType duelType = DuelTypeSelection.select(state, action, receiver);

        // In some cases it's possible that there is no challenger. This may be an exception (e.g.
        // if no goalkeeper is present) or permitted behaviour (e.g. if a team doesn't have a
        // defender to challenger an attacker in a particular area). Handling of null pointers in
        // this area needs to be improved when rules are clarified.
        Player challenger = ChallengerSelection.selectChallenger(state, duelType);

        // Duel parameters may be amended by tactics, player orders and other modifiers.
        DuelParams params = DuelParams.builder()
            .state(state)
            .duelType(duelType)
            .initiator(initiator)
            .challenger(challenger)
            .receiver(receiver)
            .origin(DuelOrigin.DEFAULT)
            .disruptor(DuelDisruptor.NONE)
            .destinationPitchArea(destinationPitchArea)
            .build();

        // The duel can be created once its result is determined. Any details about the duel that
        // need to be stored for future analysis should be set in the duel DTO and saved as part of
        // the duel here.
        DuelDTO outcome = DuelExecution.executeDuel(state, params);

        Duel duel = Duel.builder()
            .type(outcome.getParams().getDuelType())
            .initiator(initiator)
            .receiver(outcome.getParams().getReceiver())
            .challenger(outcome.getParams().getChallenger())
            .result(outcome.getResult())
            .pitchArea(outcome.getParams().getState().getBallState().getArea())
            .destinationPitchArea(outcome.getDestinationPitchArea())
            .initiatorStats(outcome.getInitiatorStats())
            .challengerStats(outcome.getChallengerStats())
            .origin(outcome.getOrigin())
            .disruptor(outcome.getDisruptor())
            .appliedPlayerOrder(outcome.getParams().getAppliedPlayerOrder())
            .build();

        // The play is now over. It is saved and the state is transitioned depending on the result.
        Play play = Play.builder()
            .duel(duel)
            .action(outcome.getParams().getDuelType().getAction())
            .clock(state.getClock())
            .ballState(outcome.getParams().getState().getBallState())
            .build();

        StringBuilder detail = new StringBuilder();
        detail.append(play.getDuel().getInitiator().getTeamRole()).append(" team in ").append(play.getDuel().getPitchArea()).append(" attempted ")
                .append(play.getAction()).append(" ("). append(Action.PASS.equals(play.getAction()) ? play.getDuel().getType() + " to " + play.getDuel().getDestinationPitchArea() : play.getDuel().getType()). append(") with result ")
                .append(play.getDuel().getResult()). append(" - ").append(play.getDuel().getInitiator().getName())
                .append(" (").append(play.getDuel().getInitiator().getPosition()). append(")")
                .append(play.getDuel().getChallenger() != null ? " challenged by " + play.getDuel().getChallenger().getName() : "")
                .append(play.getDuel().getChallenger() != null ? " (" + play.getDuel().getChallenger().getPosition() + ")": "")
                .append(play.getDuel().getReceiver() != null ? " to " + play.getDuel().getReceiver().getName() : "")
                .append(play.getDuel().getReceiver() != null ? " (" + play.getDuel().getReceiver().getPosition() + ")" : "");
        state.getRecorder().record(detail.toString(), state, GameProgressRecord.Type.SUMMARY, GameProgressRecord.DuelStage.AFTER);

        //log.debug("Play complete:\n{}", play);

        return switch (outcome.getResult()) {
            case WIN -> {
                if (action == Action.SHOOT) {
                    yield handleGoal(state, play);
                } else {
                    yield handleDuelWin(state, play);
                }
            }
            case LOSE -> handleDuelLoss(state, play);
        };
    }

    // For duel wins, we determine who takes control of the ball (depending on whether a receiver
    // was present) then increment clock and continue playing
    private static GameState handleDuelWin(GameState state, Play play) {
        Duel duel = play.getDuel();
        Player player = Optional.ofNullable(duel.getReceiver()).orElseGet(duel::getInitiator);

        PitchArea currentArea = state.getBallState().getArea();

        PitchArea playerArea;

        BallHeight ballHeight = state.getBallState().getHeight();
        if (play.getDuel().getType().movesBall()) {

            if (play.getDuel().getType().equals(DuelType.PASSING_HIGH)) {
                if (ballHeight.isLow()) {
                    state.getRecorder().record("Ball moved from low to high.", state, GameProgressRecord.Type.BALL_BEHAVIOUR, GameProgressRecord.DuelStage.AFTER);
                }
                ballHeight = BallHeight.HIGH;
            }
            if (play.getDuel().getType().equals(DuelType.PASSING_LOW)) {
                if (ballHeight.isHigh()) {
                    state.getRecorder().record("Ball moved from high to low.", state, GameProgressRecord.Type.BALL_BEHAVIOUR, GameProgressRecord.DuelStage.AFTER);
                }

                ballHeight = BallHeight.LOW;
            }

            // Moves ball to intended destination form the duel.
            if (play.getDuel().getDestinationPitchArea() == null) {
                throw new GameStateException(state, "Duel has no destination pitch area defined");
            }

            playerArea = play.getDuel().getDestinationPitchArea();

        } else {
            playerArea = currentArea;
        }
        BallState newBallState = new BallState(player, playerArea, ballHeight);

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn()) // Keep same team on turn
                    .clock(before.getClock() + play.getAction().getDuration())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .recorder(before.getRecorder())
                    .build())
            .orElseThrow();
    }

    // For duel losses, we switch sides and give the ball to the challenger.
    private static GameState handleDuelLoss(GameState state, Play play) {
        // Since the attacking team lost the last duel, the pitch area needs to be flipped so that
        // it is oriented from the perspective of the current player's team.
        PitchArea currentArea = state.getBallState().getArea().flipPerspective();

        // TODO refactor this
        Optional<BallState> fromModifier = checkModifiers(state, play);
        BallState newBallState;
        if (fromModifier.isPresent()) {
            newBallState = fromModifier.get();
        } else {
            PitchArea playerArea;
            /*
            if (play.getDuel().getType().movesBall()) {

                // If the change flank order was applied, then ball can move outisde nearby areas.
                Boolean nearbyOnly = ! PlayerOrder.CHANGE_FLANK.equals(play.getDuel().getAppliedPlayerOrder());

                playerArea = PitchAreaSelection.select(currentArea, play.getDuel().getChallenger(), nearbyOnly)
                    .orElseThrow(
                        () -> new GameStateException(state, "No pitch area to select from"));
            } else {
                playerArea = currentArea;
            }*/
            playerArea = currentArea;

            newBallState = new BallState(play.getDuel().getChallenger(), playerArea, state.getBallState().getHeight());
        }

        return Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn() == Turn.HOME ? Turn.AWAY : Turn.HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    .home(before.getHome())
                    .away(before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .recorder(before.getRecorder())
                    .build())
            .orElseThrow();
    }

    // For goals, we switch sides and increment the score
    private static GameState handleGoal(GameState state, Play play) {

        TeamState newTeamState = Optional.of(state.attackingTeam())
            .map((before) ->
                TeamState.builder()
                    .players(before.getPlayers())
                    .bench(before.getBench())
                    .tactic(before.getTactic())
                    .verticalPressure(before.getVerticalPressure())
                    .horizontalPressure(before.getHorizontalPressure())
                    .score(before.getScore() + 1)
                    .build())
            .orElseThrow();

        if (state.getBallState().getHeight().isHigh()) {
            state.getRecorder().record("Ball moved from high to low.", state, GameProgressRecord.Type.BALL_BEHAVIOUR, GameProgressRecord.DuelStage.AFTER);
        }

        // Give the ball to the kick-off player from the team that conceded the goal
        BallState newBallState = new BallState(
            KickOffSelection.selectPlayer(state, state.defendingTeam()),
            PitchArea.CENTRE_MIDFIELD, BallHeight.GROUND);

        GameState newState = Optional.of(state)
            .map((before) ->
                GameState.builder()
                    .turn(before.getTurn() == Turn.HOME ? Turn.AWAY : Turn.HOME)
                    .clock(before.getClock() + play.getAction().getDuration())
                    // Here we update the team whose turn it was since that's the team that scored
                    .home(before.getTurn() == Turn.HOME ? newTeamState : before.getHome())
                    .away(before.getTurn() == Turn.AWAY ? newTeamState : before.getAway())
                    .ballState(newBallState)
                    .plays(GameState.concatPlay(before.getPlays(), play))
                    .recorder(before.getRecorder())
                    .build())
            .orElseThrow();

        state.getRecorder().record("GOAL! ["  + newState.getHome().getScore() + "-" + newState.getAway().getScore() + "]", state, GameProgressRecord.Type.INFORMATIVE, GameProgressRecord.DuelStage.BEFORE);

        return newState;
    }

    // TODO - refactor, this should probably go somewhere else
    private static Optional<BallState> checkModifiers(GameState state, Play play) {
        if (play.getDuel().getOrigin() == DuelOrigin.DEFAULT) {
            return Optional.empty();
        }

        PlayerOrder order = play.getDuel().getInitiator().getPlayerOrder();
        if (order == PlayerOrder.CHANGE_FLANK) {
            PitchArea newArea = play.getDuel().getPitchArea().switchFile();
            Player receiver = play.getDuel().getReceiver();
            // For now, change flank

            if (play.getBallState().getHeight().isLow()) {
                state.getRecorder().record("Ball moved from low to high.", state, GameProgressRecord.Type.BALL_BEHAVIOUR, GameProgressRecord.DuelStage.AFTER);
            }

            BallState newBallState = new BallState(receiver, newArea, BallHeight.HIGH);
            return Optional.of(newBallState);
        }

        return Optional.empty();
    }
}
