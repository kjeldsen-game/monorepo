package com.kjeldsen.match.domain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.HashMap;
import java.util.Map;

import com.kjeldsen.match.domain.state.ChainActionSequence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class DuelStats {

    /*
     * Numerical data about the duel
     */

    Long id;

    // The total score that was summed up to determine who won the duel
    Integer total;
    // The relevant skill points that contributed in this duel
    Integer skillPoints;
    // Carryover from previous duel
    Integer carryover;
    // Modifier for the duel based on the random factor and last player's duel of
    // the same type
    Performance performance;

    Assistance assistance;

    Map<ChainActionSequence, Integer> chainActionBonuses;

    public static DuelStats initDefault() {
        return DuelStats.builder()
            .total(0)
            .skillPoints(0)
            .carryover(0)
            .performance(new Performance())
            .assistance(new Assistance())
            .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Performance {

        Double previousTotalImpact = 0.0;
        Double random = 0.0;
        Double total = 0.0;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Assistance {
        Double total = 0.0;
        Double adjusted = 0.0;
        Map<String, Double> teamAssistance = new HashMap<String, Double>();
        Map<ChainActionSequence, Integer> modifiers = new HashMap<>();
        Double totalModifiers = 0.0;
    }
}