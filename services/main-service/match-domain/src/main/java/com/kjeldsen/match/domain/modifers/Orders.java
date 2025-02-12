package com.kjeldsen.match.domain.modifers;

import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelOrigin;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.execution.DuelParams;
import com.kjeldsen.match.domain.selection.DuelTypeSelection;
import com.kjeldsen.match.domain.selection.PitchAreaSelection;
import com.kjeldsen.match.domain.selection.ReceiverSelection;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PitchArea.PitchFile;
import com.kjeldsen.player.domain.PitchArea.PitchRank;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Orders {

    /*
     * Applies player orders by modifying duel parameters
     */

    public static DuelParams apply(GameState state, DuelParams params, PlayerOrder order) {
        // Player orders are not applied to every single play. For now whether a player order is
        // applied is randomly determined.
        // Also, no player orders are applied if inside a chain action sequence.
        double playerOrderProbability = 0.5;
        double flip = new Random().nextDouble();
        if (flip < playerOrderProbability || state.getChainActionSequence().isActive()) {
            return params;
        }

        if (order == null) {
            return params;
        }
        return switch (order) {
            case PASS_FORWARD -> passForward(state, params);
            case LONG_SHOT -> longShot(state, params);
            case CHANGE_FLANK -> changeFlank(state, params);
            case PASS_TO_AREA -> passToArea(state, params);
            case DRIBBLE_TO_AREA -> dribbleToArea(state, params);
            case WALL_PASS -> wallPass(state, params);
            case NONE -> params;
        };
    }

    // Moves the ball forward from the midfield by passing to a forward player, if there is one
    private static DuelParams passForward(GameState state, DuelParams params) {
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }
        Optional<Player> receiver =
            ReceiverSelection.selectForward(params.getState(), params.getInitiator());
        if (receiver.isEmpty()) {
            return params;
        }

        Boolean nearbyOnly = true;
        PitchArea destinationPitchArea = PitchAreaSelection.select(state.getBallState().getArea(), receiver.get(), false)
                .orElseThrow(() -> new GameStateException(state, "No pitch area to select from"));

        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, receiver.get());

        return DuelParams.builder()
            .state(params.getState())
            .duelType(duelType)
            .initiator(params.getInitiator())
            .challenger(params.getChallenger())
            .receiver(receiver.get())
            .origin(DuelOrigin.PLAYER_ORDER)
            .disruptor(params.getDisruptor())
            .appliedPlayerOrder(PlayerOrder.PASS_FORWARD)
            .destinationPitchArea(destinationPitchArea)
            .build();
    }

    // Shoots from midfield
    private static DuelParams longShot(GameState state, DuelParams params) {
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }
        Player goalkeeper = params.getState().defendingTeam().getPlayers().stream()
            .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER)
            .findFirst()
            .orElseThrow(
                () -> new GameStateException(params.getState(), "No goalkeeper found"));

        Map<PlayerSkill, Integer> skills = goalkeeper.getSkills();
        int bonus = params.getState().getBallState().getArea().file() == PitchFile.CENTRE ? 10 : 25;
        skills.put(PlayerSkill.REFLEXES, skills.get(PlayerSkill.REFLEXES) + bonus);
        goalkeeper.setSkills(skills);

        DuelType duelType = DuelType.LONG_SHOT;

        return DuelParams.builder()
            .state(params.getState())
            .duelType(duelType)
            .initiator(params.getInitiator())
            .challenger(goalkeeper)
            .origin(DuelOrigin.PLAYER_ORDER)
            .disruptor(params.getDisruptor())
            .appliedPlayerOrder(PlayerOrder.LONG_SHOT)
            .destinationPitchArea(PitchArea.CENTRE_FORWARD)
            .build();
    }

    private static DuelParams changeFlank(GameState state, DuelParams params) {
        PitchFile file = params.getState().getBallState().getArea().file();
        PitchRank rank = params.getState().getBallState().getArea().rank();

        if (file == PitchFile.CENTRE || rank != PitchRank.MIDDLE) {
            return params;
        }
        // At this point the rank must be middle and the file is the opposite of the current one,
        // so we can deduce the area from which to select a player.
        PitchArea destinationArea =
            file == PitchFile.LEFT ? PitchArea.RIGHT_MIDFIELD : PitchArea.LEFT_MIDFIELD;

        Optional<Player> receiver = ReceiverSelection.selectFromArea(
            params.getState(), params.getInitiator(), destinationArea);

        if (receiver.isEmpty()) {
            return params;
        }

        // TODO change flank should allways be high pass
        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, receiver.get());

        return DuelParams.builder()
            .state(params.getState())
            .duelType(duelType)
            .initiator(params.getInitiator())
            .challenger(params.getChallenger())
            .receiver(receiver.get())
            .origin(DuelOrigin.PLAYER_ORDER)
            .disruptor(params.getDisruptor())
            .appliedPlayerOrder(PlayerOrder.CHANGE_FLANK)
            .destinationPitchArea(destinationArea)
            .build();
    }

    // Player pass the ball to a predefined area.
    private static DuelParams passToArea(GameState state, DuelParams params) {

        // The destination area needs to be specified for the player.
        PitchArea destinationArea = params.getInitiator().getPlayerOrderDestinationPitchArea();
        if (destinationArea == null) throw new GameStateException(params.getState(), "Pass to area player order requires a destination pitch area.");

        // Only from midfield.
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }

        // Only to midfield.
        if (destinationArea.rank() != PitchRank.MIDDLE) {
            throw new GameStateException(params.getState(), "Pass to area player order should only have midfield as origin and destination.");
        }

        // Current area should be different from destination area.
        if (params.getState().getBallState().getArea().equals(params.getInitiator().getPlayerOrderDestinationPitchArea())) {
            return params;
        }

        Optional<Player> receiver = ReceiverSelection.selectFromArea(
                params.getState(), params.getInitiator(), destinationArea);

        if (receiver.isEmpty()) {
            return params;
        }

        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, receiver.get());

        return DuelParams.builder()
                .state(params.getState())
                .duelType(duelType)
                .initiator(params.getInitiator())
                .challenger(params.getChallenger())
                .receiver(receiver.get())
                .origin(DuelOrigin.PLAYER_ORDER)
                .disruptor(params.getDisruptor())
                .appliedPlayerOrder(PlayerOrder.PASS_TO_AREA)
                .destinationPitchArea(destinationArea)
                .build();
    }

    // Player moves with the ball to a predefined area.
    private static DuelParams dribbleToArea(GameState state, DuelParams params) {

        // The destination area needs to be specified for the player.
        PitchArea destinationArea = params.getInitiator().getPlayerOrderDestinationPitchArea();
        if (destinationArea == null) throw new GameStateException(params.getState(), "Pass to area player order requires a destination pitch area.");

        // Only from midfield.
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }

        // Only to midfield or forward
        if (destinationArea.rank() != PitchRank.MIDDLE && destinationArea.rank() != PitchRank.FORWARD) {
            throw new GameStateException(params.getState(), "Pass to area player order should only have midfield as origin and destination.");
        }

        // Current area should be different from destination area.
        if (params.getState().getBallState().getArea().equals(params.getInitiator().getPlayerOrderDestinationPitchArea())) {
            return params;
        }

        DuelType duelType = DuelTypeSelection.select(state, Action.DRIBBLE, params.getInitiator());

        return DuelParams.builder()
                .state(params.getState())
                .duelType(duelType)
                .initiator(params.getInitiator())
                .challenger(params.getChallenger())
                .origin(DuelOrigin.PLAYER_ORDER)
                .disruptor(params.getDisruptor())
                .appliedPlayerOrder(PlayerOrder.DRIBBLE_TO_AREA)
                .destinationPitchArea(destinationArea)
                .build();
    }

    // Player starts a wall pass with another player.
    private static DuelParams wallPass(GameState state, DuelParams params) {

        // Only from midfield.
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }
        // Sets a receiver (replaces another if previously set).
        Optional<Player> receiver =
                ReceiverSelection.selectMidfielder(params.getState(), params.getInitiator());
        if (receiver.isEmpty()) {
            return params;
        }

        // Wall pass should only pass to midfield.
        PitchArea destinationArea = PitchAreaSelection.select(state.getBallState(), receiver.get(), true)
                .orElseThrow(() -> new GameStateException(state, "No pitch area to select from"));
        if (destinationArea.rank() != PitchRank.MIDDLE) {
            return params;
        }

        DuelType duelType = DuelTypeSelection.select(state, Action.PASS, params.getInitiator());

        return DuelParams.builder()
                .state(params.getState())
                .duelType(duelType)
                .initiator(params.getInitiator())
                .challenger(params.getChallenger())
                .receiver(receiver.get())
                .destinationPitchArea(destinationArea)
                .origin(DuelOrigin.PLAYER_ORDER)
                .disruptor(params.getDisruptor())
                .appliedPlayerOrder(PlayerOrder.WALL_PASS)
                .build();
    }
}
