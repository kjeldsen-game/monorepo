package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.PlayerSkills.PlayerSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum PlayerPosition {
    DEFENDER(Set.of(PlayerSkill.TA.withTendency(5),
        PlayerSkill.DP.withTendency(5),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.SC,
        PlayerSkill.OP,
        PlayerSkill.BC,
        PlayerSkill.PA
    )),
    MIDDLE(Set.of(PlayerSkill.PA.withTendency(4),
        PlayerSkill.BC.withTendency(3),
        PlayerSkill.OP.withTendency(3),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.TA.withTendency(2),
        PlayerSkill.DP.withTendency(2),
        PlayerSkill.SC
    )),
    FORWARD(Set.of(PlayerSkill.SC.withTendency(5),
        PlayerSkill.OP.withTendency(4),
        PlayerSkill.BC.withTendency(4),
        PlayerSkill.PA.withTendency(2),
        PlayerSkill.CO.withTendency(2),
        PlayerSkill.TA,
        PlayerSkill.DP
    ));

    private final Set<PlayerSkill> skillTendencies;
}
