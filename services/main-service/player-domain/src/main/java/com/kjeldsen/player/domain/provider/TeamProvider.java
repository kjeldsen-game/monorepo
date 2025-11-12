package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.factories.PlayerSkillsFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class TeamProvider {

    private static final Map<List<PlayerPosition>, ArchetypeDistribution> ARCHETYPE_DISTRIBUTION_MAP = Map.of(
        List.of(PlayerPosition.GOALKEEPER), new ArchetypeDistribution(3, 19, 28,
            Map.of(2, 1, 4, 1, 5, 1), Map.of(7, 1, 6, 1, 5, 1), null),
        List.of(PlayerPosition.CENTRE_BACK), new ArchetypeDistribution(5, 18, 29,
            Map.of(4, 2, 5, 1, 3, 2), Map.of(7, 1, 6, 3, 5, 1), new Integer[]{4,7}),
        List.of(PlayerPosition.LEFT_BACK, PlayerPosition.RIGHT_BACK), new ArchetypeDistribution(4, 20, 28,
            Map.of(3, 2, 5, 2), Map.of( 6, 2, 5, 2), null),
        List.of(PlayerPosition.LEFT_WINGBACK, PlayerPosition.RIGHT_WINGBACK), new ArchetypeDistribution(4, 21, 26,
            Map.of(3, 2, 4, 1, 6, 1), Map.of(7, 1, 6, 3), null),
        List.of(PlayerPosition.DEFENSIVE_MIDFIELDER), new ArchetypeDistribution(3, 20, 27,
            Map.of(3, 2, 5, 1), Map.of(6, 3), null),
        List.of(PlayerPosition.CENTRE_MIDFIELDER), new ArchetypeDistribution(5, 19, 25,
            Map.of(7, 1, 5, 1, 4, 2,3,1), Map.of(7, 1, 6, 4), new Integer[]{7,7}),
        List.of(PlayerPosition.OFFENSIVE_MIDFIELDER), new ArchetypeDistribution(1, 28, 28,
            Map.of(6, 1), Map.of(6,1), null),
        List.of(PlayerPosition.FORWARD), new ArchetypeDistribution(4, 19, 25,
            Map.of(6,1,5,2,4,1), Map.of(6,2,5,2), null),
        List.of(PlayerPosition.LEFT_WINGER, PlayerPosition.RIGHT_WINGER), new ArchetypeDistribution(3, 20, 29,
            Map.of(6,1,4,2), Map.of(7,1,6,1,5,1), null)
    );

    /**
     * Provide a players from the distribution map based on the defined rules and parameters
     * during the team initialization.
     */
    public static List<Player> provide(Team.TeamId teamId) {
        List<Player> players = new ArrayList<>();
        ARCHETYPE_DISTRIBUTION_MAP.keySet().forEach(key -> {
            ArchetypeDistribution distribution = ARCHETYPE_DISTRIBUTION_MAP.get(key);

            List<Integer> actual = expandRatings(distribution.actualRatings());
            actual.sort(Collections.reverseOrder());
            List<Integer> potential = expandRatings(distribution.potentialRatings());
            List<Integer> required = new ArrayList<>(distribution.requiredRatings() != null
                ? List.of(distribution.requiredRatings()) : List.of());

            for (int a = 0; a < distribution.count; a++ ){
                Integer actualRating;
                Integer potentialRating;
                if (!required.isEmpty()) {
                    actualRating = required.get(0);
                    potentialRating = required.get(1);
                    required.remove(actualRating);
                    required.remove(potentialRating);
                } else {
                    actualRating = actual.get(0) == 3 ? ThreadLocalRandom.current().nextInt(1,4) : actual.get(0);
                    List<Integer> possiblePotentials = potential.stream().filter(value -> value >= actualRating).toList();
                    potentialRating = possiblePotentials.get(ThreadLocalRandom.current().nextInt(possiblePotentials.size()));
                }

                // Remove that
                potential.remove(potentialRating);
                actual.remove(actual.get(0));

                PlayerPosition position = key.get(ThreadLocalRandom.current().nextInt(key.size()));
                Player p = PlayerProvider.provide(teamId, distribution.minAge, distribution.maxAge, position,
                    actualRating * 10, potentialRating  * 10);
                players.add(p);
            }
        });
        return players;
    }

    /**
     * Convert a Map of rating→count into a List with repeated ratings
     */
    private static List<Integer> expandRatings(Map<Integer, Integer> ratings) {
        List<Integer> expandedRatings = new ArrayList<>();
        ratings.forEach((rating, count) -> {
            for (int i = 0; i < count; i++) expandedRatings.add(rating);
        });
        return expandedRatings;
    }

    record ArchetypeDistribution(int count, int minAge, int maxAge,
     Map<Integer, Integer> actualRatings, Map<Integer, Integer> potentialRatings, Integer[] requiredRatings) {}
}
