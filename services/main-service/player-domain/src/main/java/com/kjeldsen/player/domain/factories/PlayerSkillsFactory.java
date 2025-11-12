package com.kjeldsen.player.domain.factories;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.provider.PlayerProvider;

import java.util.Map;

public class PlayerSkillsFactory {

    public static Map<PlayerSkill, PlayerSkills> createNonGoalkeeperSkills(PlayerPosition position) {
        return Map.of(
            PlayerSkill.SCORING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.SCORING))
                .build(),

            PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.OFFENSIVE_POSITIONING))
                .build(),

            PlayerSkill.BALL_CONTROL, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.BALL_CONTROL))
                .build(),

            PlayerSkill.PASSING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.PASSING))
                .build(),

            PlayerSkill.AERIAL, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.AERIAL))
                .build(),

            PlayerSkill.CONSTITUTION, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.CONSTITUTION))
                .build(),

            PlayerSkill.TACKLING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.TACKLING))
                .build(),

            PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.DEFENSIVE_POSITIONING))
                .build()
        );
    }

    public static Map<PlayerSkill, PlayerSkills> createGoalkeeperSkills() {
        PlayerPosition position = PlayerPosition.GOALKEEPER;
        return Map.of(
            PlayerSkill.REFLEXES, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.REFLEXES))
                .build(),

            PlayerSkill.GOALKEEPER_POSITIONING, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.GOALKEEPER_POSITIONING))
                .build(),

            PlayerSkill.INTERCEPTIONS, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.INTERCEPTIONS))
                .build(),

            PlayerSkill.ONE_ON_ONE, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.ONE_ON_ONE))
                .build(),

            PlayerSkill.CONTROL, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.CONTROL))
                .build(),

            PlayerSkill.ORGANIZATION, PlayerSkills.builder()
                .playerSkillRelevance(PlayerProvider.getSkillRelevanceBasedOnPositionAndSkill(position, PlayerSkill.ORGANIZATION))
                .build()
        );

    }
}
