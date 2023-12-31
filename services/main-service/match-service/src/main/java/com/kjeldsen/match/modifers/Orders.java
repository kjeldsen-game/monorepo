package com.kjeldsen.match.modifers;

import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.execution.DuelParams;
import com.kjeldsen.match.selection.ReceiverSelection;
import com.kjeldsen.match.state.GameStateException;
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

    public static DuelParams apply(DuelParams params, PlayerOrder order) {
        // Player orders are not applied to every single play. For now whether a player order is
        // applied is randomly determined.
        double playerOrderProbability = 0.5;
        double flip = new Random().nextDouble();
        if (flip < playerOrderProbability) {
            return params;
        }

        if (order == null) {
            return params;
        }
        return switch (order) {
            case PASS_FORWARD -> passForward(params);
            case LONG_SHOT -> longShot(params);
            case CHANGE_FLANK -> changeFlank(params);
            case NONE -> params;
        };
    }

    // Moves the ball forward from the midfield by passing to a forward player, if there is one
    private static DuelParams passForward(DuelParams params) {
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
    private static DuelParams longShot(DuelParams params) {
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

        return DuelParams.builder()
            .state(params.getState())
            .duelType(Action.SHOOT.getDuelType())
            .initiator(params.getInitiator())
            .challenger(goalkeeper)
            .origin(DuelOrigin.PLAYER_ORDER)
            .build();
    }

    private static DuelParams changeFlank(DuelParams params) {
        PitchFile file = params.getState().getBallState().getArea().file();
        PitchRank rank = params.getState().getBallState().getArea().rank();

        if (file == PitchFile.CENTRE || rank != PitchRank.MIDDLE) {
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
