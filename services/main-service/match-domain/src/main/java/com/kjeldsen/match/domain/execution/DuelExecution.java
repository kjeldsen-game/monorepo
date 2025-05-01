package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelDisruptor;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.generator.RandomGenerator;
import com.kjeldsen.match.domain.modifers.Orders;
import com.kjeldsen.match.domain.random.GausDuelRandomizer;
import com.kjeldsen.match.domain.recorder.GameProgressRecord;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class DuelExecution {

    /*
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

        return switch (params.getDuelType()) {
            case POSITIONAL -> handlePositionalDuel(params);
            case BALL_CONTROL -> handleBallControlDuel(params);
            case DRIBBLE -> handleDribbleDuel(params);
            case PASSING_LOW, PASSING_HIGH -> handlePassDuel(params);
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> handleShotDuel(params);
        };
    }

    // Three things affect the positional duel outcome: (1) the skill points of the
    // players, (2) the
    // performance of each player, which is randomly generated for each duel and (3)
    // the assistance
    // each player gets from their teammates. All of these points are added and the
    // player with the
    // highest sum wins the duel.
    public static DuelDTO handlePositionalDuel(DuelParams params) {
        GameState state = params.getState();
        Player initiator = params.getInitiator();
        Player challenger = params.getChallenger();

        // How to calculate the assistance
        // 1. Get the teamAssistance by player
        // 2. Get the modifiers
        // 3. Sum the modifiers together
        // 4. Sum all pieces together
        // Caulcate adjusted values whenn both of this are done

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

        log.info("assisatnce refustdsajkjksa");
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
            challengerStats = DuelStats.builder()
                    .skillPoints(0)
                    .performance(new DuelStats.Performance())
                    .assistance(DuelStats.Assistance.builder()
                        .modifiers(new HashMap<>())
                        .totalModifiers(0.0)
                        .teamAssistance(new HashMap<>())
                        .total(0.0)
                        .adjusted(0.0).build())
                    .total(0)
                    .build();
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

    // Performs the calculations for positional duels, which require assistance. The
    // total in
    // DuelStats is used to determine the overall winner of the duel.
    private static DuelStats buildPositionalDuelStats(
            GameState state,
            Player player,
            DuelRole role,
            DuelStats.Assistance assistance) {

        int skillPoints = player.duelSkill(DuelType.POSITIONAL, role, state);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, DuelType.POSITIONAL, role);

        // Apply chain action sequences skill modifiers.
        int chainActionSequenceModifier = 0;
        if (ChainActionSequence.WALL_PASS == state.getChainActionSequence()
                && state.lastPlay().isPresent()
                && state.lastPlay().get().getChainActionSequence().isActive()) {
            int passingSkill = player.getSkills().get(PlayerSkill.PASSING);

            double lowerLimit = -15;
            double upperLimit = 15;

            // Linear transformation. PA = 0 -> OP = -15. PA = 100 -> OP = 15.
            chainActionSequenceModifier = (int) (lowerLimit + ((upperLimit - lowerLimit) * (passingSkill / 100.0)));
            String detail = "Wall pass skill modification. For POSITIONAL " + skillPoints + " and PASSING "
                    + passingSkill + ", the modifier is " + chainActionSequenceModifier
                    + " for a resulting POSITION skill of " + (skillPoints + chainActionSequenceModifier);
            state.getRecorder().record(detail, state, GameProgressRecord.Type.CALCULATION,
                    GameProgressRecord.DuelStage.DURING);

        }

        int total = skillPoints + chainActionSequenceModifier + performance.getTotal().intValue() +
            assistance.getAdjusted().intValue();

        return DuelStats.builder()
            .skillPoints(skillPoints)
            .performance(performance)
            .assistance(assistance)
            .total(total)
            .build();
    }

    // Ball control follows a positional duel. Here the factors to determine a
    // winner are (1) skill
    // points, (2) performance, and (3) the assistance carryover from the positional
    // duel.
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
                : DuelStats.builder()
                        .skillPoints(0)
                        .performance(new DuelStats.Performance())
                        .carryover(0)
                        .total(0)
                        .build();

        DuelResult result = params.getChallenger() != null ? (initiatorStats.getTotal() > challengerStats.getTotal())
                ? DuelResult.WIN
                : DuelResult.LOSE : DuelResult.WIN;

        return DuelDTO.builder()
                .result(result)
                .initiatorStats(initiatorStats)
                .challengerStats(challengerStats)
                .origin(params.getOrigin())
                .disruptor(params.getDisruptor())
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

        DuelResult duelResult;
        DuelDTO result;

        // GK interception is a special type of interception. it's not a duel per se,
        // but a dice roll.
        boolean goalkeeperInterceptionOcurred = false;
        if (PitchArea.CENTRE_FORWARD.equals(params.getDestinationPitchArea())
                && DuelType.PASSING_HIGH.equals(params.getDuelType())) {
            goalkeeperInterceptionOcurred = rollGoalkeeperInterception(state);
        }

        if (goalkeeperInterceptionOcurred) {

            // The duel is interrupted by the GK interception event. The GK already won the
            // duel.
            duelResult = DuelResult.LOSE;

            // Change the challenger to the goalkeeper.
            challenger = state.defendingTeam().getPlayers().stream()
                    .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER).findFirst().get();

            DuelParams modifiedParams = DuelParams.builder()
                    .state(state)
                    .duelType(params.getDuelType())
                    .initiator(initiator)
                    .challenger(challenger)
                    .receiver(receiver)
                    .origin(params.getOrigin())
                    .disruptor(DuelDisruptor.GOALKEEPER_INTERCEPTION)
                    .destinationPitchArea(destinationPitchArea)
                    .build();

            DuelStats challengerStats = buildDuelStats(
                    state,
                    challenger,
                    params.getDuelType(),
                    DuelRole.CHALLENGER);

            result = DuelDTO.builder()
                    .result(duelResult)
                    .initiatorStats(initiatorStats)
                    .challengerStats(challengerStats)
                    .origin(modifiedParams.getOrigin())
                    .disruptor(modifiedParams.getDisruptor())
                    .destinationPitchArea(destinationPitchArea)
                    .params(modifiedParams)
                    .build();

        } else {

            DuelStats challengerStats = buildDuelStats(
                    state,
                    challenger,
                    params.getDuelType(),
                    DuelRole.CHALLENGER);

            // Passing duels always succeed for now but this leaves the possibility of an
            // interception
            duelResult = DuelResult.WIN;

            result = DuelDTO.builder()
                    .result(duelResult)
                    .initiatorStats(initiatorStats)
                    .challengerStats(challengerStats)
                    .origin(params.getOrigin())
                    .destinationPitchArea(destinationPitchArea)
                    .params(params)
                    .build();
        }

        return result;
    }

    private static boolean rollGoalkeeperInterception(GameState state) {

        Player defendingGoalkeeper = state.defendingTeam().getPlayers().stream()
                .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER).findFirst().get();

        Integer diceRoll = (int) (Math.random() * 100);
        Integer goalkeepingInterceptionsSkill = defendingGoalkeeper.getSkills().get(PlayerSkill.INTERCEPTIONS);
        Integer interceptionsAttemptScore = goalkeepingInterceptionsSkill - diceRoll;

        Boolean interceptionOcurred = false;
        Double interceptionChance = Math.random();
        if (interceptionsAttemptScore > 75) {
            if (interceptionChance < .5) {
                interceptionOcurred = true;
            }
        } else if (interceptionsAttemptScore > 50) {
            if (interceptionChance < .25) {
                interceptionOcurred = true;
            }
        } else if (interceptionsAttemptScore > 30) {
            if (interceptionChance < .1) {
                interceptionOcurred = true;
            }
        }

        StringBuilder detail = new StringBuilder();
        if (interceptionOcurred) {
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

        return interceptionOcurred;
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


        DuelStats initiatorStats = buildDuelStats(
                state,
                initiator,
                params.getDuelType(),
                DuelRole.INITIATOR);

        Integer total = initiatorStats.getTotal();
        log.info("Total shot checking stuff = {}", total);
        // Chance that the shot is missed and goalkeeper don't have to make save
        if (total < 100) {
            double difference = (double) (100 - total) / 4;
            int succeed = RandomGenerator.randomInt(0, 100);
            log.info("Diffreence value = {} and succeeed = {}", difference, succeed);
            // Shot is missed
            if (succeed < difference) {
                log.info("Shot duel resulted in missed shot!");
                return DuelDTO.builder()
                    .result(DuelResult.LOSE)
                    .initiatorStats(initiatorStats)
                    .challengerStats(null)
                    .origin(params.getOrigin())
                    .disruptor(params.getDisruptor())
                    .params(params)
                    .build();
            }
        }

        if (challenger.getPosition() != PlayerPosition.GOALKEEPER) {
            throw new IllegalStateException("Player defending shot is not a GOALKEEPER.");
        }

        DuelStats challengerStats = buildDuelStats(
                state,
                challenger,
                params.getDuelType(),
                DuelRole.CHALLENGER);

        Boolean goalkeeperReachedTheBall = initiatorStats.getTotal() < challengerStats.getTotal(); // initiator striker
        // log.info("InitiatorStats {} challengerStats {} booleanResult {}",
        // initiatorStats.getTotal(), challengerStats.getTotal(),
        // goalkeeperReachedTheBall);
        // TODO refactor this
        Boolean fumbleOccurred = false;
        if (goalkeeperReachedTheBall) {

            Integer diceRoll = (int) (Math.random() * 100);
            Integer controlSkill = challenger.getSkillValue(PlayerSkill.CONTROL);
            Integer controlResult = diceRoll - controlSkill;
            Double controlSuccessChance = Math.random();

            if (controlSkill < diceRoll) {
                if (controlResult > 75) {
                    if (controlSuccessChance < 0.1) {
                        fumbleOccurred = true;
                    }
                } else if (controlResult > 50) {
                    if (controlSuccessChance < 0.25) {
                        fumbleOccurred = true;
                    }
                } else if (controlResult > 30) {
                    if (controlSuccessChance < 0.50) {
                        fumbleOccurred = true;
                    }
                }
            }

            StringBuilder detail = new StringBuilder();
            if (fumbleOccurred) {
                detail.append("The Goalkeeper made a fumble:");
            } else {
                detail.append("The Goalkeeper managed to control the ball");
            }
            detail.append("Dice roll: ").append(diceRoll)
                    .append(", Control Skill: ").append(controlSkill)
                    .append(", Control result: ").append(controlResult)
                    .append(", Control success change: ").append(controlSuccessChance);
            state.getRecorder().record(detail.toString(), state, GameProgressRecord.Type.CALCULATION,
                    GameProgressRecord.DuelStage.DURING);

        }

        DuelResult result = !goalkeeperReachedTheBall || fumbleOccurred ? DuelResult.WIN : DuelResult.LOSE;

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

        int skillPoints = player.duelSkill(type, role, state);
        // double performance = (double) DuelRandomization.performance(state, player,
        // type, role);
        DuelStats.Performance performance = GausDuelRandomizer.performance(player, state, type, role);

        if (type == DuelType.POSITIONAL) {
            throw new IllegalArgumentException("Positional duel not allowed here");
        }

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
