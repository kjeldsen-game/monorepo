package com.kjeldsen.match.entities.duel;

import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.state.BallHeight;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.List;

public enum DuelType {

    // MVP duels
    PASSING_LOW,
    PASSING_HIGH,
    POSITIONAL,
    BALL_CONTROL,
    LOW_SHOT,
    ONE_TO_ONE_SHOT,
    HEADER_SHOT,
    LONG_SHOT;

    // Future duels
    // AGGRESSION,
    // CORNER,
    // FREE_KICKING,
    // GOAL_KICKING,
    // PENALTY;

    // These are the actions that can be taken by a player on the team that started the duel, if the
    // duel is won. For example, if a player starts a positional duel and wins it, then that player
    // can either pass or shoot. In scenarios where the ball is passed to another player, the player
    // who receives the ball is the one who can take one of the actions here.
    public List<Action> winActions() {
        return switch (this) {
            case PASSING_LOW, PASSING_HIGH -> List.of(Action.POSITION);
            case POSITIONAL -> List.of(Action.PASS, Action.SHOOT);
            case BALL_CONTROL -> List.of(Action.PASS, Action.SHOOT);
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> List.of(); // Goal - no valid actions available after scoring
        };
    }

    // If a player initiates a duel and loses, then the player who won the duel (on the opposing
    // team) can take one of the actions here. For example, if a player initiates a shot duel
    // and loses, then the opposing player (the goalkeeper) only has a pass action available.
    public List<Action> loseActions() {
        return switch (this) {
            case PASSING_LOW, PASSING_HIGH -> List.of(Action.PASS);
            case POSITIONAL -> List.of(Action.TACKLE);
            case BALL_CONTROL -> List.of(Action.PASS, Action.SHOOT);
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> List.of(Action.PASS); // Goalkeeper save
        };
    }

    // The skills involved in the particular duel type. This depends on whether the player in the
    // duel is the initiator or challenger.
    public List<PlayerSkill> requiredSkills(DuelRole role, BallHeight ballHeight) {
        if (role == DuelRole.INITIATOR) {
            return switch (this) {
                case PASSING_LOW -> List.of(PlayerSkill.PASSING);
                case PASSING_HIGH -> List.of(PlayerSkill.PASSING);
                case POSITIONAL -> List.of(PlayerSkill.OFFENSIVE_POSITIONING);
                case BALL_CONTROL -> BallHeight.HIGH.equals(ballHeight) ?
                    List.of(PlayerSkill.TACKLING, PlayerSkill.AERIAL): List.of(PlayerSkill.TACKLING);
                case LOW_SHOT -> List.of(PlayerSkill.SCORING);
                case ONE_TO_ONE_SHOT -> List.of(PlayerSkill.SCORING);
                case HEADER_SHOT -> List.of(PlayerSkill.SCORING, PlayerSkill.AERIAL);
                case LONG_SHOT -> List.of(PlayerSkill.SCORING, PlayerSkill.PASSING);
            };
        } else {
            return switch (this) {
                case PASSING_LOW -> List.of(PlayerSkill.INTERCEPTING);
                case PASSING_HIGH -> List.of(PlayerSkill.INTERCEPTING);
                case POSITIONAL -> List.of(PlayerSkill.DEFENSIVE_POSITIONING);
                case BALL_CONTROL -> BallHeight.HIGH.equals(ballHeight) ?
                        List.of(PlayerSkill.BALL_CONTROL, PlayerSkill.AERIAL): List.of(PlayerSkill.BALL_CONTROL);
                case LOW_SHOT -> List.of(PlayerSkill.REFLEXES);
                case ONE_TO_ONE_SHOT -> List.of(PlayerSkill.ONE_ON_ONE);
                case HEADER_SHOT -> List.of(PlayerSkill.REFLEXES);
                case LONG_SHOT -> List.of(PlayerSkill.REFLEXES);
            };

        }
    }

    public Action getAction() {
        return switch (this) {
            case PASSING_LOW, PASSING_HIGH -> Action.PASS;
            case POSITIONAL -> Action.POSITION;
            case BALL_CONTROL -> Action.TACKLE;
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> Action.SHOOT;
        };
    }

    // Certain duels involve the movement of the ball whereas others occur in the same area
    public boolean movesBall() {
        return List.of(DuelType.PASSING_LOW, DuelType.PASSING_HIGH, DuelType.LOW_SHOT, DuelType.ONE_TO_ONE_SHOT, DuelType.HEADER_SHOT).contains(this);
    }

    public boolean isPassing() {
        return List.of(
                        DuelType.PASSING_LOW,
                        DuelType.PASSING_HIGH)
                .contains(this);
    }

    public boolean isShot() {
        return List.of(
                        DuelType.LOW_SHOT,
                        DuelType.ONE_TO_ONE_SHOT,
                        DuelType.HEADER_SHOT,
                        DuelType.LONG_SHOT)
                .contains(this);
    }

}
