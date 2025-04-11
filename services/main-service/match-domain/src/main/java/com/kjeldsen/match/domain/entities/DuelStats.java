package com.kjeldsen.match.domain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
    // How much support the player got from each player on his team
    Map<String, Integer> teamAssistance;
    // How much support the player got from his team as a whole
    Integer assistance;
    // Carryover from previous duel
    Integer carryover;
    // Modifier for the duel based on the random factor and last player's duel of
    // the same type
    Performance performance;

    Map<ChainActionSequence, Integer> chainActionBonuses;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Performance {

        Double previousTotalImpact = 0.0;
        Double random = 0.0;
        Double total = 0.0;
    }
}