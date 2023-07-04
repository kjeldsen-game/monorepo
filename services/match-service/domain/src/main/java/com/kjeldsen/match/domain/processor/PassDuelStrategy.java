package com.kjeldsen.match.domain.processor;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.type.DuelResult;

import java.util.Map;

public class PassDuelStrategy implements DuelStrategy {
    @Override
    public DuelResult execute(Map<Player.PlayerSkill, Integer> attackerSkills, Map<Player.PlayerSkill, Integer> defenderSkills) {
        Integer attackerSkill = attackerSkills.get(Player.PlayerSkill.PASSING);
        Integer defenderSkill = defenderSkills.get(Player.PlayerSkill.TACKLING);

        return DuelSkillMatchup.tryPass(attackerSkill, defenderSkill);
    }
}
