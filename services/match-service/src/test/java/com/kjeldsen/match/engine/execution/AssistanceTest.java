package com.kjeldsen.match.engine.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.kjeldsen.match.engine.random.DuelRandomization;
import com.kjeldsen.match.entities.duel.DuelRole;
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

        Map<DuelRole, Integer> normalizedAssistanceTotals =
            Assistance.normalizeAssistance(initiatorTeamAssistance, challengerTeamAssistance);

        assertEquals(0, normalizedAssistanceTotals.get(DuelRole.INITIATOR));
        assertEquals(0, normalizedAssistanceTotals.get(DuelRole.CHALLENGER));
    }

    @Test
    void largeRangeAssistanceNormalizedCorrectly() {
        Map<String, Integer> initiatorTeamAssistance = Map.of(
            "Alice", 100,
            "Bob", 100,
            "Charlie", 100,
            "Diana", 100
        );

        Map<String, Integer> challengerTeamAssistance = Map.of(
            "Alice", 0,
            "Bob", 0,
            "Charlie", 0,
            "Diana", 0
        );

        Map<DuelRole, Integer> normalizedAssistanceTotals =
            Assistance.normalizeAssistance(initiatorTeamAssistance, challengerTeamAssistance);

        assertEquals(
            Assistance.MAX_ASSISTANCE, normalizedAssistanceTotals.get(DuelRole.INITIATOR));
        assertEquals(
            0, normalizedAssistanceTotals.get(DuelRole.CHALLENGER));
    }

    @Test
    void assistanceLimitsMatch() {
        // These values are currently the same, and we ensure their equality here, but they may
        // change to be different
        assertEquals(Assistance.MAX_ASSISTANCE, DuelRandomization.MAX_ASSISTANCE);
        assertEquals(Assistance.MIN_ASSISTANCE, DuelRandomization.MIN_ASSISTANCE);
    }
}