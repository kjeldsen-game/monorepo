package com.kjeldsen.match.engine.entities.duel;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.SkillType;
import java.util.List;

public enum DuelType {

    // MVP duels
    PASSING,
    POSITIONAL,
    BALL_CONTROL,
    SHOT;

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
            case PASSING -> List.of(Action.POSITION);
            case POSITIONAL -> List.of(Action.PASS, Action.SHOOT);
            case BALL_CONTROL -> List.of(Action.PASS, Action.SHOOT);
            case SHOT -> List.of(); // Goal - no valid actions available after scoring
        };
    }

    // If a player initiates a duel and loses, then the player who won the duel (on the opposing
    // team) can take one of the actions here. For example, if a player initiates a shot duel
    // and loses, then the opposing player (the goalkeeper) only has a pass action available.
    public List<Action> loseActions() {
        return switch (this) {
            case PASSING -> List.of(Action.PASS);
            case POSITIONAL -> List.of(Action.TACKLE);
            case BALL_CONTROL -> List.of(Action.PASS, Action.SHOOT);
            case SHOT -> List.of(Action.PASS); // Goalkeeper save
        };
    }

    // Some duels require a teammate to be selected to receive the ball.
    public boolean requiresReceiver() {
        return switch (this) {
            case PASSING -> true;
            case POSITIONAL, BALL_CONTROL, SHOT -> false;
        };
    }

    // The skill involved in the particular duel type. This depends on whether the player in the
    // duel is the initiator or challenger.
    public SkillType requiredSkill(DuelRole role) {
        if (role == DuelRole.INITIATOR) {
            return switch (this) {
                case PASSING -> SkillType.PASSING;
                case POSITIONAL -> SkillType.OFFENSIVE_POSITIONING;
                case BALL_CONTROL -> SkillType.TACKLING;
                case SHOT -> SkillType.SCORING;
            };
        } else {
            return switch (this) {
                case PASSING -> SkillType.INTERCEPTING;
                case POSITIONAL -> SkillType.DEFENSIVE_POSITIONING;
                case BALL_CONTROL -> SkillType.BALL_CONTROL;
                case SHOT -> SkillType.REFLEXES;
            };

        }
    }
}
