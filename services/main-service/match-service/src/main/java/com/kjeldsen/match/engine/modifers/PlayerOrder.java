package com.kjeldsen.match.engine.modifers;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PitchArea.PitchFile;
import com.kjeldsen.match.engine.entities.PitchArea.PitchRank;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.entities.duel.DuelOrigin;
import com.kjeldsen.match.engine.execution.DuelParams;
import com.kjeldsen.match.engine.selection.ReceiverSelection;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.models.Player;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public enum PlayerOrder {

    /*
     * Each player can receive an order from the manager. The order will affect how the player
     * carries out his action. If an order cannot be carried out because the team has not been,
     * configured correctly, or any other reason, then the order will not be applied and the default
     * action will be used as a fallback.
     */

    // Midfield orders

    PASS_FORWARD,
    LONG_SHOT,
    CHANGE_FLANK,

    // Disabled for player

    NONE;

    public DuelParams apply(DuelParams params) {
        // Player orders are not applied to every single play. For now whether a player order is
        // applied is randomly determined.
        double playerOrderProbability = 0.5;
        double flip = new Random().nextDouble();
        if (flip < playerOrderProbability) {
            return params;
        }
        return switch (this) {
            case PASS_FORWARD -> passForward(params);
            case LONG_SHOT -> longShot(params);
            case CHANGE_FLANK -> changeFlank(params);
            case NONE -> params;
        };
    }

    // Moves the ball forward from the midfield by passing to a forward player, if there is one
    private DuelParams passForward(DuelParams params) {
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }
        Optional<Player> receiver =
            ReceiverSelection.selectForward(params.getState(), params.getInitiator());
        if (receiver.isEmpty()) {
            return params;
        }
        return DuelParams.builder()
            .state(params.getState())
            .duelType(Action.PASS.getDuelType())
            .initiator(params.getInitiator())
            .challenger(params.getChallenger())
            .receiver(receiver.get())
            .origin(DuelOrigin.PLAYER_ORDER)
            .build();
    }

    // Shoots from midfield
    private DuelParams longShot(DuelParams params) {
        if (params.getState().getBallState().getArea().rank() != PitchRank.MIDDLE) {
            return params;
        }
        Player goalkeeper = params.getState().defendingTeam().getPlayers().stream()
            .filter(player -> player.getPosition() == PlayerPosition.GOALKEEPER)
            .findFirst()
            .orElseThrow(
                () -> new GameStateException(params.getState(), "No goalkeeper found"));

        Map<SkillType, Integer> skills = goalkeeper.getSkills();
        int bonus = params.getState().getBallState().getArea().file() == PitchFile.CENTER ? 10 : 25;
        skills.put(SkillType.REFLEXES, skills.get(SkillType.REFLEXES) + bonus);
        goalkeeper.setSkills(skills);

        return DuelParams.builder()
            .state(params.getState())
            .duelType(Action.SHOOT.getDuelType())
            .initiator(params.getInitiator())
            .challenger(goalkeeper)
            .origin(DuelOrigin.PLAYER_ORDER)
            .build();
    }

    private DuelParams changeFlank(DuelParams params) {
        PitchFile file = params.getState().getBallState().getArea().file();
        PitchRank rank = params.getState().getBallState().getArea().rank();

        if (file == PitchFile.CENTER || rank != PitchRank.MIDDLE) {
            return params;
        }
        // At this point the rank must be middle and the file is the opposite of the current one,
        // so we can deduce the area from which to select a player.
        PitchArea area =
            file == PitchFile.LEFT ? PitchArea.RIGHT_MIDFIELD : PitchArea.LEFT_MIDFIELD;

        Optional<Player> receiver = ReceiverSelection.selectFromArea(
            params.getState(), params.getInitiator(), area);

        if (receiver.isEmpty()) {
            return params;
        }
        return DuelParams.builder()
            .state(params.getState())
            .duelType(Action.PASS.getDuelType())
            .initiator(params.getInitiator())
            .challenger(params.getChallenger())
            .receiver(receiver.get())
            .origin(DuelOrigin.PLAYER_ORDER)
            .build();
    }
}
