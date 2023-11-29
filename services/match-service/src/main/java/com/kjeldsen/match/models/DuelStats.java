package com.kjeldsen.match.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@Entity
@Table(name = "duel_stats")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class DuelStats {

    /*
     * Numerical data about the duel
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // The total score that was summed up to determine who won the duel
    Integer total;
    // How well the player performed in the duel (randomly generated for each duel)
    Integer performance;
    // The relevant skill points that contributed in this duel
    Integer skillPoints;
    // How much support the player got from each player on his team
    @ElementCollection
    Map<String, Integer> teamAssistance;
    // How much support the player got from his team as a whole
    Integer assistance;
    // Carryover from previous duel
    Integer carryover;
}
