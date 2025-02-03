package com.kjeldsen.match.domain.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.random.DuelRandomization;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AssistanceTest {

    @Test
    void equalAssistanceNormalizedCorrectly() {
        Map<String, Integer> initiatorTeamAssistance = Map.of(
            "Alice", 50,
            "Bob", 50,
            "Charlie", 50,
            "Diana", 50
        );

        Map<String, Integer> challengerTeamAssistance = Map.of(
            "Alice", 50,
            "Bob", 50,
            "Charlie", 50,
            "Diana", 50
        );

        Map<DuelRole, Integer> adjustedAssistanceTotals =
            Assistance.adjustAssistance(initiatorTeamAssistance, challengerTeamAssistance);

        assertEquals(0, adjustedAssistanceTotals.get(DuelRole.INITIATOR));
        assertEquals(0, adjustedAssistanceTotals.get(DuelRole.CHALLENGER));
    }

    @Test
    void largeRangeAssistanceNormalizedCorrectly() {
        // A difference of 625 is enough to reach the max assistance limit
        Map<String, Integer> initiatorTeamAssistance = Map.of(
            "Alice", 100,
            "Bob", 100,
            "Charlie", 100,
            "Diana", 100,
            "Eve", 100,
            "Frank", 100,
            "George", 100
        );

        Map<String, Integer> challengerTeamAssistance = Map.of(
            "Alice", 0,
            "Bob", 0,
            "Charlie", 0,
            "Diana", 0
        );

        Map<DuelRole, Integer> adjustedAssistanceTotals =
            Assistance.adjustAssistance(initiatorTeamAssistance, challengerTeamAssistance);

        assertEquals(
            Assistance.MAX_ASSISTANCE, adjustedAssistanceTotals.get(DuelRole.INITIATOR));
        assertEquals(
            0, adjustedAssistanceTotals.get(DuelRole.CHALLENGER));
    }

    @Test
    void assistanceLimitsMatch() {
        // These values are currently the same, and we ensure their equality here, but they may
        // change to be different
        assertEquals(Assistance.MAX_ASSISTANCE, DuelRandomization.MAX_ASSISTANCE);
        assertEquals(Assistance.MIN_ASSISTANCE, DuelRandomization.MIN_ASSISTANCE);
    }
}