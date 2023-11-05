package com.kjeldsen.match.engine.entities.duel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonInclude(Include.NON_NULL)
public class DuelStats {

    /*
     * Numerical data about the duel
     */

    // The total score that was summed up to determine who won the duel
    Integer total;
    // How well the player performed in the duel (randomly generated for each duel)
    Integer performance;
    // The relevant skill points that contributed in this duel
    Integer skillPoints;
    // How much support the player got from each player on his team
    Map<String, Integer> teamAssistance;
    // How much support the player got from his team as a whole
    Integer assistance;
    // Carryover from previous duel
    Integer carryover;
}
