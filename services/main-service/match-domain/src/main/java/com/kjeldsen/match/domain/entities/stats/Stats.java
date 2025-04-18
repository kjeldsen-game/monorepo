package com.kjeldsen.match.domain.entities.stats;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Stats {

//    StatsId id = StatsId.generate();

    // Score opportunities stats
    Integer score = 0;
    Integer missed = 0;

    // Pass stats
    Integer passes = 0;
    Integer failedPasses = 0;

    // Tackle Stats
    Integer tackles = 0;

    // Goalkeeper stats + team total saves
    Integer saved = 0;



    public record StatsId(String value) {

        public static Stats.StatsId generate() {
            return new Stats.StatsId(UUID.randomUUID().toString());
        }

        public static Stats.StatsId of(String value) {
            return new Stats.StatsId(value);
        }
    }
}
