package com.kjeldsen.match.domain.processor;

import com.kjeldsen.match.domain.type.DuelResult;
import lombok.Getter;

@Getter
public class DuelSkillMatchup {
    // TODO: Include modifiers and return a DuelResultWrapper
    public static DuelResult tryPass(Integer attackerSkillPoints, Integer defenderSkillPoints) {
        return attackerSkillPoints > defenderSkillPoints ? DuelResult.WIN : DuelResult.LOSE;
    }
}
