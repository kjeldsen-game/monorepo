package com.kjeldsen.player.domain.provider;

import com.github.javafaker.Faker;
import com.kjeldsen.player.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerProvider {
    public static final int MAX_SKILL_VALUE = 100;

    public static String name() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }


    public static Map<PlayerSkill, PlayerSkills> skillsBasedOnTendency(PlayerPositionTendency positionTendencies, Integer totalPoints) {

        HashMap<PlayerSkill, PlayerSkills> skillPointsMap = positionTendencies
                .getTendencies()
                .keySet()
                .stream()
                .collect(HashMap::new, (map, skill) -> map.put(skill, PlayerSkills.builder()
                        .actual(0)
                        .potential(0)
                        .playerSkillRelevance(getSkillRelevanceBasedOnPositionAndSkill(positionTendencies.getPosition(), skill))
                        .build()), HashMap::putAll);
                ;

        Set<PlayerSkill> excludedSkills = new HashSet<>();

        IntStream.range(0, totalPoints).forEach(i -> {
            Optional<PlayerSkill> skill = positionTendencies.getRandomSkillBasedOnTendency(excludedSkills);
            // if no skill is found, it means that all skills have reached the max value

            if (skill.isEmpty()) {
                return;
            }
            PlayerSkills values = skillPointsMap.get(skill.get());
            values.increaseActualPoints(1);

            if (values.getActual() == MAX_SKILL_VALUE) {
                excludedSkills.add(skill.get());
            }
            skillPointsMap.put(skill.get(), values);
        });
        skillPointsMap.values().forEach(PlayerSkills::initializePotentialPoints);
        return skillPointsMap;
    }

    public static PlayerSkillRelevance getSkillRelevanceBasedOnPositionAndSkill(PlayerPosition position, PlayerSkill skill){
        switch (skill){
            case SCORING: switch (position){
                case DEFENSIVE_MIDFIELDER, FORWARD, STRIKER: return PlayerSkillRelevance.CORE;
                case LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER, AERIAL_FORWARD, AERIAL_STRIKER: return PlayerSkillRelevance.SECONDARY;
                case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK, LEFT_WINGBACK, RIGHT_WINGBACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER: return PlayerSkillRelevance.RESIDUAL;
            }
            case OFFENSIVE_POSITIONING: switch (position){
                case DEFENSIVE_MIDFIELDER, OFFENSIVE_MIDFIELDER, LEFT_WINGER, RIGHT_WINGER, FORWARD, STRIKER: return PlayerSkillRelevance.CORE;
                case LEFT_WINGBACK, RIGHT_WINGBACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER, AERIAL_FORWARD, AERIAL_STRIKER: return PlayerSkillRelevance.SECONDARY;
                case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK: return PlayerSkillRelevance.RESIDUAL;
            }
            case BALL_CONTROL: switch (position){
                case DEFENSIVE_MIDFIELDER, OFFENSIVE_MIDFIELDER, LEFT_WINGER, RIGHT_WINGER, FORWARD, STRIKER: return PlayerSkillRelevance.CORE;
                case LEFT_WINGBACK, RIGHT_WINGBACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER, AERIAL_FORWARD, AERIAL_STRIKER: return PlayerSkillRelevance.SECONDARY;
                case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK: return PlayerSkillRelevance.RESIDUAL;
            }
            case PASSING: switch (position){
                case CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER, LEFT_WINGER, RIGHT_WINGER, OFFENSIVE_MIDFIELDER: return PlayerSkillRelevance.CORE;
                case LEFT_BACK, RIGHT_BACK, LEFT_WINGBACK, RIGHT_WINGBACK, DEFENSIVE_MIDFIELDER, FORWARD, AERIAL_FORWARD, STRIKER, AERIAL_STRIKER: return PlayerSkillRelevance.SECONDARY;
                case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER: return PlayerSkillRelevance.RESIDUAL;
            }
            case AERIAL: switch (position){
                case AERIAL_CENTRE_BACK, DEFENSIVE_MIDFIELDER, AERIAL_FORWARD, AERIAL_STRIKER: return PlayerSkillRelevance.CORE;
                case CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK, FORWARD, STRIKER: return PlayerSkillRelevance.SECONDARY;
                case LEFT_WINGBACK, RIGHT_WINGBACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER, LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER: return PlayerSkillRelevance.RESIDUAL;
            }
            case CONSTITUTION: switch (position){
                case CENTRE_BACK, AERIAL_CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK, LEFT_WINGBACK, RIGHT_WINGBACK, DEFENSIVE_MIDFIELDER, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER, LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER, FORWARD, AERIAL_FORWARD, STRIKER, AERIAL_STRIKER: return PlayerSkillRelevance.SECONDARY;
            }
            case TACKLING: switch (position){
                case CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK, LEFT_WINGBACK, RIGHT_WINGBACK: return PlayerSkillRelevance.CORE;
                case AERIAL_CENTRE_BACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER: return PlayerSkillRelevance.SECONDARY;
                case DEFENSIVE_MIDFIELDER, LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER, FORWARD, AERIAL_FORWARD, STRIKER, AERIAL_STRIKER: return PlayerSkillRelevance.RESIDUAL;
            }
            case DEFENSIVE_POSITIONING: switch (position){
                case CENTRE_BACK, SWEEPER, LEFT_BACK, RIGHT_BACK, LEFT_WINGBACK, RIGHT_WINGBACK, DEFENSIVE_MIDFIELDER: return PlayerSkillRelevance.CORE;
                case AERIAL_CENTRE_BACK, CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER: return PlayerSkillRelevance.SECONDARY;
                case LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER, FORWARD, AERIAL_FORWARD, STRIKER, AERIAL_STRIKER: return PlayerSkillRelevance.RESIDUAL;
            }
            case REFLEXES, GOALKEEPER_POSITIONING: switch (position){
                case GOALKEEPER: return PlayerSkillRelevance.CORE;
            }
            case INTERCEPTIONS, ONE_ON_ONE: switch (position){
                case GOALKEEPER: return PlayerSkillRelevance.SECONDARY;
            }
            case CONTROL, ORGANIZATION: switch (position){
                case GOALKEEPER: return PlayerSkillRelevance.RESIDUAL;
            }
            throw new IllegalStateException("Unexpected value on the following position: " + position);
        }
        throw new IllegalStateException("Unexpected value on the following skill: " + skill);
    }

    public static Player generate(Team.TeamId teamId, PlayerPositionTendency positionTendencies, PlayerCategory playerCategory, int totalPoints) {
<<<<<<< HEAD
        Player player = Player.builder()
                .id(Player.PlayerId.generate())
                .name(name())
                .age(age())
                .position(positionTendencies.getPosition())
                .actualSkills(skillsBasedOnTendency(positionTendencies, totalPoints))
                .playerOrder(PlayerOrder.NONE)
                .status(PlayerStatus.INACTIVE)
                .teamId(teamId)
                .category(playerCategory)
                .economy(Player.Economy.builder().build())
                .build();
=======
        Player player;
        if(playerCategory.name().equals(PlayerCategory.JUNIOR.name())){
            player = Player.builder()
                    .id(Player.PlayerId.generate())
                    .name(name())
                    .age(PlayerAge.generateAgeOfAPlayer(playerCategory))
                    .position(positionTendencies.getPosition())
                    .status(PlayerStatus.INACTIVE)
                    .playerOrder(PlayerOrder.NONE)
                    .actualSkills(skillsBasedOnTendency(positionTendencies, totalPoints))
                    .teamId(teamId)
                    .category(PlayerCategory.JUNIOR)
                    .economy(Player.Economy.builder().build())
                    .build();
        } else {
            player = Player.builder()
                    .id(Player.PlayerId.generate())
                    .name(name())
                    .age(PlayerAge.generateAgeOfAPlayer(playerCategory))
                    .position(positionTendencies.getPosition())
                    .status(PlayerStatus.INACTIVE)
                    .playerOrder(PlayerOrder.NONE)
                    .actualSkills(skillsBasedOnTendency(positionTendencies, totalPoints))
                    .teamId(teamId)
                    .category(PlayerCategory.SENIOR)
                    .economy(Player.Economy.builder().build())
                    .build();
        }
>>>>>>> 034bd1b (Fixing player category - Player aging done)
        player.negotiateSalary();
        return player;
    }

    public static PlayerSkill randomSkill() {
        PlayerSkill[] allSkills = PlayerSkill.values();
        int random = (int) (Math.random() * allSkills.length);
        return allSkills[random];
    }

    public static PlayerSkill randomSkillForSpecificPlayer(Optional<Player> player) {
        Map<PlayerSkill, PlayerSkills> skills = player.get().getActualSkills();
        List<PlayerSkill> allSkills = skills.keySet().stream().toList();
        int random = (int) (Math.random() * allSkills.size());
        return allSkills.get(random);

    public static Player generateDefault() {
        return generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES, PlayerCategory.JUNIOR, 200);
    }

    public static PlayerPosition position() {
        return PlayerPosition.values()[RandomUtils.nextInt(0, PlayerPosition.values().length)];
    }

}
