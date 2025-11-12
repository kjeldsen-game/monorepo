package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.utils.RatingUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SkillsProvider {

    private final static Random random = new Random();

    /**
     * Provide the skill values for players based on the rating of the player. After the skill values are generated
     * rating of the player is recalculated based on the skills.
     */
    public static void provide(Player player) {
        Map<PlayerSkill, Double> ratingMap = RatingUtils.getRatingMapForPosition(player.getPreferredPosition());
        // Normalize values from the rating map, so the values are in decimal ex 55 -> 0.55
        var normalized = ratingMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Math.ceil(e.getValue() / 100 * 1000) / 1000.0
            ));

        Map<PlayerSkill, Double> coreSkills = filterSkillsByRelevance(player.getActualSkills(), normalized, PlayerSkillRelevance.CORE);
        Map<PlayerSkill, Double> secondarySkills = filterSkillsByRelevance(player.getActualSkills(), normalized, PlayerSkillRelevance.SECONDARY);
        Map<PlayerSkill, Double> residualSkills = filterSkillsByRelevance(player.getActualSkills(), normalized, PlayerSkillRelevance.RESIDUAL);

        computeSkills(player.getRating(), player.getActualSkills(), coreSkills, secondarySkills, residualSkills);
        RatingProvider.getRating(player, ratingMap);
    }

    /**
     * Filters the player's skills by a given relevance level and returns a map of corresponding skill weights.
     */
    private static Map<PlayerSkill, Double> filterSkillsByRelevance(Map<PlayerSkill, PlayerSkills> skills,
        Map<PlayerSkill, Double> ratingMap, PlayerSkillRelevance relevance) {
        return skills.entrySet()
            .stream()
            .filter(e -> e.getValue().getPlayerSkillRelevance() == relevance)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> ratingMap.get(e.getKey())
            ));
    }

    /**
     * Generates a map of random weight values for the given set of player skills.
     * Each skill is assigned a random value between 0.8 and 1.2.
     */
    private static Map<PlayerSkill, Double> randomMap(Set<PlayerSkill> skills) {
        Map<PlayerSkill, Double> result = new HashMap<>();
        for (PlayerSkill skill : skills) {
            result.put(skill, 0.8 + (1.2 - 0.8) * random.nextDouble());
        }
        return result;
    }

    /**
     * Computes and assigns skill values to a player based on their rating and the weighted relevance of each skill group.
     * Randomization is applied to introduce variability in skill values within reasonable bounds.
     */
    private static void computeSkills(Rating rating, Map<PlayerSkill, PlayerSkills> playerSkills,
        Map<PlayerSkill, Double> coreSkills, Map<PlayerSkill, Double> secondarySkills, Map<PlayerSkill, Double> residualSkills) {

        double coreSum = rating.getActual() * coreSkills.values().stream().mapToDouble(Double::doubleValue).sum();
        double secondarySum = rating.getActual() * secondarySkills.values().stream().mapToDouble(Double::doubleValue).sum();
        double potentialCoreSum = rating.getPotential() * coreSkills.values().stream().mapToDouble(Double::doubleValue).sum();
        double potentialSecondarySum = rating.getPotential() * secondarySkills.values().stream().mapToDouble(Double::doubleValue).sum();

        provideSkillValuesByRelevance(playerSkills, coreSkills, coreSum, potentialCoreSum);
        provideSkillValuesByRelevance(playerSkills, secondarySkills, secondarySum, potentialSecondarySum);

        for (PlayerSkill skill : residualSkills.keySet()) {
            int value = 1 + random.nextInt(15);
            PlayerSkills skillValues = playerSkills.get(skill);
            skillValues.setActual(value);
            skillValues.setPotential(ThreadLocalRandom.current().nextInt(value, value + 5));
        }
    }

    /**
     * Distributes actual and potential skill values based on the {@link PlayerSkillRelevance}.
     * Random variation is applied to each skill to introduce variability in the distribution.
     */
    private static void provideSkillValuesByRelevance(
        Map<PlayerSkill, PlayerSkills> playerSkills , Map<PlayerSkill, Double> skills,
        double actualSum, double potentialSum) {
        if (!skills.isEmpty()) {
            Map<PlayerSkill, Double> rands = randomMap(skills.keySet());
            double total = skills.entrySet().stream()
                .mapToDouble(e -> e.getValue() * rands.get(e.getKey()))
                .sum();
            for (PlayerSkill skill : skills.keySet()) {
                PlayerSkills skillValue = playerSkills.get(skill);
                skillValue.setActual((int) Math.round(rands.get(skill) * actualSum / total));
                skillValue.setPotential((int) Math.round(rands.get(skill) * potentialSum / total));
            }
        }
    }
}
