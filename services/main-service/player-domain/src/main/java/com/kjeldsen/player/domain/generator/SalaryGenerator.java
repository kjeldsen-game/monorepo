package com.kjeldsen.player.domain.generator;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkills;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SalaryGenerator {

    public static BigDecimal salary(Player player) {
        BigDecimal salary = BigDecimal.ZERO;
        for (PlayerSkills playerSkills : player.getActualSkills().values()) {
            salary = salary.add(
                BigDecimal.valueOf(100)
                    .multiply(BigDecimal.valueOf(playerSkills.getActual()))
                    .multiply(BigDecimal.valueOf(calculateSalaryMultiplier(playerSkills.getActual()))));
        }
        return salary;
    }

    private static double calculateSalaryMultiplier(Integer skillPoints) {
        if (skillPoints <= 20) {
            return .25;
        } else if (skillPoints <= 30) {
            return .35;
        } else if (skillPoints <= 40) {
            return .50;
        } else if (skillPoints <= 50) {
            return .75;
        } else if (skillPoints <= 60) {
            return 1;
        } else if (skillPoints <= 70) {
            return 1.5;
        } else if (skillPoints <= 80) {
            return 2;
        } else if (skillPoints <= 90) {
            return 2.5;
        } else if (skillPoints <= 100) {
            return 3;
        }
        return 0;
    }

}
