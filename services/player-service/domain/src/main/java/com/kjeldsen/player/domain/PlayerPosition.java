package com.kjeldsen.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum PlayerPosition {
    DEFENDER(Set.of(PlayerSkill.TACKLING.withTendency(5),
        PlayerSkill.DEFENSE_POSITION.withTendency(5),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.SCORE,
        PlayerSkill.OFFENSIVE_POSITION,
        PlayerSkill.BALL_CONTROL,
        PlayerSkill.PASSING
    )),
    MIDDLE(Set.of(PlayerSkill.PASSING.withTendency(4),
        PlayerSkill.BALL_CONTROL.withTendency(3),
        PlayerSkill.OFFENSIVE_POSITION.withTendency(3),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.TACKLING.withTendency(2),
        PlayerSkill.DEFENSE_POSITION.withTendency(2),
        PlayerSkill.SCORE
    )),
    FORWARD(Set.of(PlayerSkill.SCORE.withTendency(5),
        PlayerSkill.OFFENSIVE_POSITION.withTendency(4),
        PlayerSkill.BALL_CONTROL.withTendency(4),
        PlayerSkill.PASSING.withTendency(2),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.TACKLING,
        PlayerSkill.DEFENSE_POSITION
    ));

    private final Set<PlayerSkill> skillTendencies;
}
