package com.kjeldsen.match.engine.processing;

import com.kjeldsen.match.models.Team;
import java.util.Map;

public final class Attendance {

    // Attendance rate per fan loyalty tier (1 = casual, 5 = ultra-loyalist)
    private static final Map<Integer, Double> TIER_RATES =
        Map.of(1, 0.20, 2, 0.40, 3, 0.60, 4, 0.80, 5, 0.95);

    private static final double HOME_BONUS = 0.10;

    // Returns the absolute number of fans attending a home match.
    // Formula: Σ(fans_in_tier × tier_rate) / total_fans, plus 10% home bonus, capped at 100%.
    public static int compute(Team homeTeam) {
        Map<Integer, Integer> fans = homeTeam.getFans();
        if (fans == null || fans.isEmpty()) {
            return 0;
        }
        int totalFans = fans.values().stream().mapToInt(Integer::intValue).sum();
        if (totalFans == 0) {
            return 0;
        }
        double weightedSum = fans.entrySet().stream()
            .mapToDouble(e -> e.getValue() * TIER_RATES.getOrDefault(e.getKey(), 0.0))
            .sum();
        double baseRate = weightedSum / totalFans;
        double finalRate = Math.min(1.0, baseRate + HOME_BONUS);
        return (int) Math.round(totalFans * finalRate);
    }
}
