package com.kjeldsen.match.domain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
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
