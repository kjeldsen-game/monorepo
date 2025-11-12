package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Rating;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class RatingProvider {

    /**
     * Calculates the player's actual and potential ratings based on their skills and weighted rating map.
     *
     * @param player     {@link Player} whose rating should be calculated and updated
     * @param ratingMap  {@link Map} of {@link PlayerSkill} to {@link Double} representing skill weight percentages
     */
    public static void getRating(Player player, Map<PlayerSkill, Double> ratingMap) {
        Map<PlayerSkill, PlayerSkills> skills = player.getActualSkills();
        if (skills == null) {
            throw new IllegalArgumentException();
        }

        if (skills.size() != ratingMap.size()) {
            throw new IllegalArgumentException();
        }

        if (player.getRating() == null) {
            player.setRating(Rating.builder().build());
        }

        player.getRating().setRatings(Map.of(
            "actual", calculateRating(skills, ratingMap, PlayerSkills::getActual),
            "potential", calculateRating(skills, ratingMap, PlayerSkills::getPotential)
        ));
    }

    /**
    * Get the average rating of the given players.
    */
    public static Rating getTeamRating(List<Player> players) {
        DoubleSummaryStatistics actualRating = new DoubleSummaryStatistics();
        DoubleSummaryStatistics potentialRating = new DoubleSummaryStatistics();

        players.stream()
            .map(Player::getRating)
            .filter(Objects::nonNull)
            .forEach(r -> {
                actualRating.accept(r.getActual());
                potentialRating.accept(r.getPotential());
            });

        return Rating.builder()
            .actual(Math.round(actualRating.getAverage() * 10.0) / 10.0)
            .potential(Math.round(potentialRating.getAverage() * 10.0) / 10.0)
            .build();
    }

    /**
     * Common method to calculate the total rating of a certain type for a player.
     *
     * @param skills      Map of {@link PlayerSkill} to {@link PlayerSkills} representing the player's skills
     * @param ratingMap   Map of {@link PlayerSkill} to {@link Double} representing skill weight percentages
     * @param valueGetter Function that extracts the skill value to use in the calculation
     *
     * @return the calculated weighted rating as a {@code double}, rounded to one decimal place
     */
    private static double calculateRating(
        Map<PlayerSkill, PlayerSkills> skills,
        Map<PlayerSkill, Double> ratingMap,
        Function<PlayerSkills, Integer> valueGetter
    ) {
        return Math.round(
            skills.entrySet().stream()
                .mapToDouble(e -> valueGetter.apply(e.getValue()) *
                    ratingMap.getOrDefault(e.getKey(), 0.0) / 100.0)
                .sum() * 10
        ) / 10.0;
    }
}
