package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.type.DuelResult;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PlayerSkill;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import java.util.Optional;
import java.util.Random;

public class DuelExecution {

    /*
     * Determines the outcome of duels.
     */

    public static DuelResult executeDuel(
        GameState state,
        DuelType duelType,
        Player player,
        Player challenger,
        Player receiver) {

        return switch (duelType) {
            case PASSING -> handlePassDuel(state, player, receiver, challenger);
            case SHOT -> handleShotDuel(state, player, challenger);
            case POSITIONAL -> handlePositionalDuel(state, player, challenger);
            case BALL_CONTROL -> handleBallControlDuel(state, player, challenger);
        };
    }

    public static DuelResult handleBallControlDuel(
        GameState state, Player player, Player challenger) {
        if (player.getSkillPoints(PlayerSkill.ATTACKING)
            > challenger.getSkillPoints(PlayerSkill.DEFENDING)) {
            return DuelResult.WIN;
        }
        return DuelResult.LOSE;
    }

    public static DuelResult handlePositionalDuel(
        GameState state, Player player, Player challenger) {
        if (player.getSkillPoints(PlayerSkill.ATTACKING)
            > challenger.getSkillPoints(PlayerSkill.DEFENDING)) {
            return DuelResult.WIN;
        }
        return DuelResult.LOSE;
    }

    public static DuelResult handlePassDuel(
        GameState state, Player player, Player receiver, Player challenger) {
        // Passing duels always succeed for now but this leaves the possibility of an interception
        Optional.ofNullable(receiver)
            .orElseThrow(() -> new GameStateException("A receiver was not set for a pass duel"));

        return DuelResult.WIN;
    }

    public static DuelResult handleShotDuel(GameState state, Player shooter, Player goalkeeper) {
        if (shooter.getSkillPoints(PlayerSkill.SCORING)
            > goalkeeper.getSkillPoints(PlayerSkill.DEFENDING)) {
            return DuelResult.WIN;
        }
        return DuelResult.LOSE;
    }
}
