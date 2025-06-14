package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.*;
import com.kjeldsen.match.domain.generator.RandomGenerator;
import com.kjeldsen.match.domain.modifers.Orders;
import com.kjeldsen.match.domain.random.GausDuelRandomizer;
import com.kjeldsen.match.domain.recorder.GameProgressRecord;
import com.kjeldsen.match.domain.selection.ReceiverSelection;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class DuelExecution {

    /**
     * Determines the outcome of duels. Each duel execution method returns a
     * DuelDTO.
     */
    public static DuelDTO executeDuel(GameState state, DuelParams params) {

        // Before we execute a passing duel (besides kickoff or after a shot), player
        // orders are given the opportunity
        // to change of the parameters for execution - the action (duelType) and
        // challenger - as well
        // as the skill points of players involved (this is only temporary).
        if (params.getDuelType().isPassing()) {
            boolean firstPlay = params.getState().getClock() == 1;
            boolean afterShot = state.lastPlay().isPresent() && Action.SHOOT.equals(state.lastPlay().get().getAction());
            if (!firstPlay && !afterShot) {
                params = Orders.apply(state, params, params.getInitiator().getPlayerOrder());
            }
        }
       // TODO velke pivo + koniferka
        return switch (params.getDuelType()) {
            case POSITIONAL -> handlePositionalDuel(params);
            case BALL_CONTROL -> handleBallControlDuel(params);
            case DRIBBLE -> handleDribbleDuel(params);
            case PASSING_LOW, PASSING_HIGH, THROW_IN -> handlePassDuel(params);
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> handleShotDuel(params);
        };
    }

    /**
     * Positional duel outcome is affected by three things that are skill points, performance and
     * assistance from the teammates. Positional duel can be affected by some of the tactics or modifiers
     */
    public static DuelDTO handlePositionalDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        // Calculate the teamAssistance for each player in duel and adjust based on scaling
        Map<String, Double> initiatorTeamAssistance = AssistanceProvider.getTeamAssistance(state, initiator, DuelRole.INITIATOR);
        Map<String, Double> challengerTeamAssistance = AssistanceProvider.getTeamAssistance(state, challenger, DuelRole.CHALLENGER);

        GameState.ChainAction counterAttackAction = state.getChainActions().getOrDefault(
            ChainActionSequence.COUNTER_ATTACK, new GameState.ChainAction());

        int counterAttackBonus = 0;
        // Counter attack bonus is active
        if (counterAttackAction != null && counterAttackAction.getActive()) {
            // This is used for the initiator
            counterAttackBonus = counterAttackAction.getBonus();
            log.info("Counter Attack usage !!!! with bonus = {} for turn {} usage = {}", counterAttackBonus, counterAttackAction.getTurn(), counterAttackAction.getUsage());
        }

        Map<ChainActionSequence, Integer> initiatorModifiers = new HashMap<>();
        initiatorModifiers.put(ChainActionSequence.COUNTER_ATTACK, counterAttackBonus);


        // This return total, adjusted and the team assistance
        Map<DuelRole, DuelStats.Assistance> adjustedAssistanceByDuelRole = AssistanceProvider.buildAssistanceByDuelRole(
            initiatorTeamAssistance, challengerTeamAssistance, initiatorModifiers);

        DuelStats initiatorStats = buildPositionalDuelStats(
                state,
                initiator,
                DuelRole.INITIATOR,
                adjustedAssistanceByDuelRole.get(DuelRole.INITIATOR));

        DuelStats challengerStats;
        DuelResult result;

        // In some situations it's possible that there is no challenger present because
        // of rules
        // preventing a defender from being into two consecutive duels. In this case the
        // duel is automatically won by the initiator.

        if (challenger == null) {
            challengerStats = DuelStats.initDefault();
            result = DuelResult.WIN;
        } else {
            challengerStats = buildPositionalDuelStats(
                    state,
                    challenger,
                    DuelRole.CHALLENGER,
                    adjustedAssistanceByDuelRole.get(DuelRole.CHALLENGER));

            result = (initiatorStats.getTotal() > challengerStats.getTotal())
                    ? DuelResult.WIN
                    : DuelResult.LOSE;
        }

        return DuelDTO.builder()
                .result(result)
                .initiatorStats(initiatorStats)
                .challengerStats(challengerStats)
                .origin(params.getOrigin())
                .disruptor(params.getDisruptor())
                .params(params)
                .build();
    }


    /**
     * Performs the calculations for positional duels, which require assistance. The
     * total in DuelStats is used to determine the overall winner of the duel.
     */
    private static DuelStats buildPositionalDuelStats(
            GameState state,
            Player player,
            DuelRole role,
            DuelStats.Assistance assistance) {

        int skillPoints = player.duelSkill(DuelType.POSITIONAL, role, state);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, DuelType.POSITIONAL, role);

        // Apply chain action sequences skill modifiers.
        int chainActionSequenceModifier = 0;
//        if (ChainActionSequence.WALL_PASS == state.getChainActionSequence()
//                && state.lastPlay().isPresent()
//                && state.lastPlay().get().getChainActionSequence().isActive()) {
//            int passingSkill = player.getSkills().get(PlayerSkill.PASSING);
//
//            double lowerLimit = -15;
//            double upperLimit = 15;
//
//            // Linear transformation. PA = 0 -> OP = -15. PA = 100 -> OP = 15.
//            chainActionSequenceModifier = (int) (lowerLimit + ((upperLimit - lowerLimit) * (passingSkill / 100.0)));
//            String detail = "Wall pass skill modification. For POSITIONAL " + skillPoints + " and PASSING "
//                    + passingSkill + ", the modifier is " + chainActionSequenceModifier
//                    + " for a resulting POSITION skill of " + (skillPoints + chainActionSequenceModifier);
//            state.getRecorder().record(detail, state, GameProgressRecord.Type.CALCULATION,
//                    GameProgressRecord.DuelStage.DURING);
//
//        }

        Optional<Play> previousPlay = state.lastPlay();
        if (role.equals(DuelRole.CHALLENGER) && previousPlay.isPresent()
            && previousPlay.get().getDuel().getDuelDisruption() != null
            && previousPlay.get().getDuel().getDuelDisruption().getDestinationPitchArea() != PitchArea.OUT_OF_BOUNDS) {
            assistance.getModifiers().put(ChainActionSequence.MISSED_PASS, 50);
            assistance.setTotal(assistance.getTotal() + assistance.getModifiersSum());
        }

        int total = skillPoints + chainActionSequenceModifier + performance.getTotal().intValue() +
            assistance.getAdjusted().intValue() + assistance.getModifiersSum().intValue();

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .assistance(assistance)
            .total(total)
            .build();
    }

    /**
    * Ball control follows a positional duel. Here the factors to determine a winner
    * are skill points, performance and the carryover from positional duel.
    */
    public static DuelDTO handleBallControlDuel(DuelParams params) {

        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        DuelStats initiatorStats = buildDuelStats(
                state,
                initiator,
                DuelType.BALL_CONTROL,
                DuelRole.INITIATOR);

        DuelStats challengerStats = params.getChallenger() != null ? buildDuelStats(
                state,
                challenger,
                DuelType.BALL_CONTROL,
                DuelRole.CHALLENGER)
                : DuelStats.initWithoutAssistance();

        DuelResult result = params.getChallenger() != null ? (initiatorStats.getTotal() > challengerStats.getTotal())
                ? DuelResult.WIN
                : DuelResult.LOSE : DuelResult.WIN;

        return DuelDTO.builder()
                .result(result)
                .initiatorStats(initiatorStats)
                .challengerStats(challengerStats)
                .origin(params.getOrigin())
                .params(params)
                .build();
    }

    public static DuelDTO handleDribbleDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        DuelStats initiatorStats = buildDuelStats(
                state,
                initiator,
                params.getDuelType(),
                DuelRole.INITIATOR);

        DuelStats challengerStats = buildDuelStats(
                state,
                challenger,
                params.getDuelType(),
                DuelRole.CHALLENGER);

        // Dribbling duels always succeed for now but this leaves the possibility of an
        // interception exactly as passing.
        DuelResult result = result = DuelResult.WIN;

        return DuelDTO.builder()
                .result(result)
                .initiatorStats(initiatorStats)
                .challengerStats(challengerStats)
                .origin(params.getOrigin())
                .destinationPitchArea(params.getDestinationPitchArea())
                .disruptor(params.getDisruptor())
                .params(params)
                .build();
    }

    public static DuelDTO handlePassDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();
        Player receiver = params.getReceiver();
        PitchArea destinationPitchArea = params.getDestinationPitchArea();

        Optional.ofNullable(receiver)
                .orElseThrow(
                        () -> new GameStateException(state, "A receiver was not set for a pass duel"));

        DuelStats initiatorStats = buildDuelStats(
                state,
                initiator,
                params.getDuelType(),
                DuelRole.INITIATOR);

        Integer total = initiatorStats.getTotal();

        Optional<DuelDisruption> disruption = DisruptionExecution.executeDisruption(params,
            DuelDisruptor.MISSED_PASS, state, total);

        DuelResult duelResult;
        DuelDTO result;
        DuelStats challengerStats;

        // There was missed pass to other area
        if (disruption.isPresent() && disruption.get().getDestinationPitchArea() == PitchArea.OUT_OF_BOUNDS) {
            challengerStats = DuelStats.initWithoutAssistance();
            duelResult = DuelResult.LOSE;
        } else {
            challengerStats = buildDuelStats(
                state,
                challenger,
                params.getDuelType(),
                DuelRole.CHALLENGER);
            duelResult = DuelResult.WIN;
        }

        result = DuelDTO.builder()
            .result(duelResult)
            .initiatorStats(initiatorStats)
            .challengerStats(challengerStats)
            .origin(params.getOrigin())
            .destinationPitchArea(destinationPitchArea)
            .params(params)
            .duelDisruption(disruption.orElse(null))
            .build();

        // GK interception is a special type of interception. it's not a duel per se,
        // but a dice roll.
//        boolean goalkeeperInterceptionOccurred = false;
//        if (PitchArea.CENTRE_FORWARD.equals(params.getDestinationPitchArea())
//                && DuelType.PASSING_HIGH.equals(params.getDuelType())) {
//            goalkeeperInterceptionOccurred = rollGoalkeeperInterception(state);
//        }

//        if (goalkeeperInterceptionOccurred) {
//
//            // The duel is interrupted by the GK interception event. The GK already won the
//            // duel.
//            duelResult = DuelResult.LOSE;
//
//            // Change the challenger to the goalkeeper.
//            challenger = state.defendingTeam().getPlayers().stream()
//                    .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER).findFirst().get();
//
//            modifiedParams = modifiedParams.toBuilder()
//                    .duelType(params.getDuelType())
//                    .disruptor(DuelDisruptor.GOALKEEPER_INTERCEPTION)
//                    .build();
//
//            DuelStats challengerStats = buildDuelStats(
//                    state,
//                    challenger,
//                    params.getDuelType(),
//                    DuelRole.CHALLENGER);
//
//            result = DuelDTO.builder()
//                    .result(duelResult)
//                    .initiatorStats(initiatorStats)
//                    .challengerStats(challengerStats)
//                    .origin(modifiedParams.getOrigin())
//                    .disruptor(modifiedParams.getDisruptor())
//                    .destinationPitchArea(destinationPitchArea)
//                    .params(modifiedParams)
//                    .build();
//
//        } else {


//        }

        return result;
    }

    private static boolean rollGoalkeeperInterception(GameState state) {

        Player defendingGoalkeeper = state.defendingTeam().getPlayers().stream()
                .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER).findFirst().get();

        Integer diceRoll = (int) (Math.random() * 100);
        Integer goalkeepingInterceptionsSkill = defendingGoalkeeper.getSkills().get(PlayerSkill.INTERCEPTIONS);
        Integer interceptionsAttemptScore = goalkeepingInterceptionsSkill - diceRoll;

        Boolean interceptionOccurred = false;
        Double interceptionChance = Math.random();
        if (interceptionsAttemptScore > 75) {
            if (interceptionChance < .5) {
                interceptionOccurred = true;
            }
        } else if (interceptionsAttemptScore > 50) {
            if (interceptionChance < .25) {
                interceptionOccurred = true;
            }
        } else if (interceptionsAttemptScore > 30) {
            if (interceptionChance < .1) {
                interceptionOccurred = true;
            }
        }

        StringBuilder detail = new StringBuilder();
        if (interceptionOccurred) {
            detail.append("Interception ocurred! ");
        } else {
            detail.append("Possible interception not attempted: ");
        }
        detail.append("Dice roll: ").append(diceRoll)
                .append(", GK interceptions skills:").append(goalkeepingInterceptionsSkill)
                .append(", Interception ocurrence score (skill - roll): ").append(interceptionsAttemptScore)
                .append(", Interception chance: ").append(interceptionChance);

        state.getRecorder().record(detail.toString(), state, GameProgressRecord.Type.CALCULATION,
                GameProgressRecord.DuelStage.DURING);

        return interceptionOccurred;
    }

    // A shot duel can follow many duel types. The contributing factors are (1)
    // skill points, (2)
    // performance, and (3) the carryover, which is calculated from the total of the
    // previous duel.
    public static DuelDTO handleShotDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        // Run a calculation to determinate if the initiator missed the shot

        if (challenger.getPosition() != PlayerPosition.GOALKEEPER) {
            throw new IllegalStateException("Player defending shot is not a GOALKEEPER.");
        }

        DuelStats initiatorStats = buildDuelStats(
                state,
                initiator,
                params.getDuelType(),
                DuelRole.INITIATOR);

        Integer total = initiatorStats.getTotal();

        Optional<DuelDisruption> disruption = DisruptionExecution.executeDisruption(params,
            DuelDisruptor.MISSED_SHOT, state, total);

        if (disruption.isPresent()) {
            return DuelDTO.builder()
                .result(DuelResult.LOSE)
                .initiatorStats(initiatorStats)
                .challengerStats(null)
                .origin(params.getOrigin())
                .duelDisruption(disruption.get())
                .params(params)
                .build();
        }

        DuelStats challengerStats = buildDuelStats(
                state,
                challenger,
                params.getDuelType(),
                DuelRole.CHALLENGER);

        boolean goalkeeperReachedTheBall = initiatorStats.getTotal() < challengerStats.getTotal(); // initiator striker
        Optional<DuelDisruption> duelDisruption = Optional.empty();

        if (goalkeeperReachedTheBall) {
            duelDisruption = DisruptionExecution.executeDisruption(params,
                DuelDisruptor.GOALKEEPER_FUMBLE, state, null);
        }

        // Goalkeeper did not save shot or goalkeeper fumble happened
        DuelResult result = !goalkeeperReachedTheBall || duelDisruption.isPresent() ?
            DuelResult.WIN : DuelResult.LOSE;

        return DuelDTO.builder()
                .result(result)
                .initiatorStats(initiatorStats)
                .challengerStats(challengerStats)
                .origin(params.getOrigin())
                .disruptor(params.getDisruptor())
                .params(params)
                .build();
    }

    // Performs the calculations for non-positional duels. These duels involve carry
    // over rather
    // than assistance. The total in these stats is also used to determine the
    // overall duel winner.
    private static DuelStats buildDuelStats(GameState state, Player player, DuelType type, DuelRole role) {
        // Case for the THROW_IN because challenger is not present
        if (type.equals(DuelType.THROW_IN) && role.equals(DuelRole.CHALLENGER)) {
            return DuelStats.initWithoutAssistance();
        }

        if (type == DuelType.POSITIONAL) {
            throw new IllegalArgumentException("Positional duel not allowed here");
        }

        int skillPoints = player.duelSkill(type, role, state);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, type, role);

        Map<DuelRole, Integer> carryovers = Carryover.getCarryover(state);
        int carryover = carryovers.get(role);

        int total = skillPoints + performance.getTotal().intValue() + carryover;

        return DuelStats.builder()
                .skillPoints(skillPoints)
                .performance(performance)
                .carryover(carryover)
                .total(total)
                .build();
    }

    // The number of consecutive losses for a player for a given duel type, counting
    // from the most
    // recent play. Returns zero if the player has won the most recent duel of this
    // type.
    public static int consecutiveLosses(GameState state, Player player, DuelType type) {
        return (int) state.getPlays().stream()
                .sorted(Comparator.comparingInt(Play::getClock).reversed())
                .map(Play::getDuel)
                .filter(
                        duel -> duel.getInitiator().equals(player) || duel.getChallenger().equals(player))
                .filter(duel -> duel.getType() == type)
                .takeWhile(duel -> duel.getResult() == DuelResult.LOSE)
                .count();
    }
}
