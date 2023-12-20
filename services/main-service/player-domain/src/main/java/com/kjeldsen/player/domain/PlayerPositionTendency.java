package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Data
@Document(collection = "PlayerPositionTendencies")
@TypeAlias("PlayerPositionTendency")

public class PlayerPositionTendency {

    public static final PlayerPositionTendency DEFAULT_CENTRE_BACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.CENTRE_BACK)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.AERIAL_CENTRE_BACK)
            .tendencies(Map.of(
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_SWEEPER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.SWEEPER)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_LEFT_BACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.LEFT_BACK)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(3).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_RIGHT_BACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.RIGHT_BACK)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(3).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_LEFT_WINGBACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.LEFT_WINGBACK)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(3).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_RIGHT_WINGBACK_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.RIGHT_WINGBACK)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(3).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.DEFENSIVE_MIDFIELDER)
            .tendencies(Map.of(
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_CENTRE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.CENTRE_MIDFIELDER)
            .tendencies(Map.of(
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_LEFT_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.LEFT_MIDFIELDER)
            .tendencies(Map.of(
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_RIGHT_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.RIGHT_MIDFIELDER)
            .tendencies(Map.of(
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_LEFT_WINGER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.LEFT_WINGER)
            .tendencies(Map.of(
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.OFFENSIVE_MIDFIELDER)
            .tendencies(Map.of(
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_RIGHT_WINGER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.RIGHT_WINGER)
            .tendencies(Map.of(
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_FORWARD_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.FORWARD)
            .tendencies(Map.of(
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(7).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_AERIAL_FORWARD_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.AERIAL_FORWARD)
            .tendencies(Map.of(
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(2).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_STRIKER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.STRIKER)
            .tendencies(Map.of(
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(5).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_AERIAL_STRIKER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.AERIAL_STRIKER)
            .tendencies(Map.of(
                    PlayerSkill.AERIAL, PlayerSkills.builder().actual(9).potential(0).build(),
                    PlayerSkill.SCORING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.BALL_CONTROL, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.PASSING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.CONSTITUTION, PlayerSkills.builder().actual(4).potential(0).build(),
                    PlayerSkill.TACKLING, PlayerSkills.builder().actual(1).potential(0).build(),
                    PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder().actual(1).potential(0).build()
            )).build();

    public static final PlayerPositionTendency DEFAULT_GOALKEEPER_TENDENCIES = PlayerPositionTendency.builder()
            .position(PlayerPosition.GOALKEEPER)
            .tendencies(Map.of(
                    PlayerSkill.REFLEXES, PlayerSkills.builder().actual(8).potential(0).build(),
                    PlayerSkill.GOALKEEPER_POSITIONING, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.INTERCEPTIONS, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.ONE_ON_ONE, PlayerSkills.builder().actual(6).potential(0).build(),
                    PlayerSkill.CONTROL, PlayerSkills.builder().actual(3).potential(0).build(),
                    PlayerSkill.ORGANIZATION, PlayerSkills.builder().actual(3).potential(0).build()
            )).build();


    public static final List<PlayerPositionTendency> DEFAULT_TENDENCIES = List.of(
            DEFAULT_CENTRE_BACK_TENDENCIES,
            DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES,
            DEFAULT_SWEEPER_TENDENCIES,
            DEFAULT_LEFT_BACK_TENDENCIES,
            DEFAULT_RIGHT_BACK_TENDENCIES,
            DEFAULT_LEFT_WINGBACK_TENDENCIES,
            DEFAULT_RIGHT_WINGBACK_TENDENCIES,
            DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
            DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
            DEFAULT_LEFT_MIDFIELDER_TENDENCIES,
            DEFAULT_RIGHT_MIDFIELDER_TENDENCIES,
            DEFAULT_LEFT_WINGER_TENDENCIES,
            DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES,
            DEFAULT_RIGHT_WINGER_TENDENCIES,
            DEFAULT_FORWARD_TENDENCIES,
            DEFAULT_AERIAL_FORWARD_TENDENCIES,
            DEFAULT_STRIKER_TENDENCIES,
            DEFAULT_AERIAL_STRIKER_TENDENCIES,
            DEFAULT_GOALKEEPER_TENDENCIES);


    public static PlayerPositionTendency getDefault(PlayerPosition position) {
        return switch (position) {
            case CENTRE_BACK -> DEFAULT_CENTRE_BACK_TENDENCIES;
            case AERIAL_CENTRE_BACK -> DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES;
            case SWEEPER -> DEFAULT_SWEEPER_TENDENCIES;
            case LEFT_BACK -> DEFAULT_LEFT_BACK_TENDENCIES;
            case RIGHT_BACK -> DEFAULT_RIGHT_BACK_TENDENCIES;
            case LEFT_WINGBACK -> DEFAULT_LEFT_WINGBACK_TENDENCIES;
            case RIGHT_WINGBACK -> DEFAULT_RIGHT_WINGBACK_TENDENCIES;
            case DEFENSIVE_MIDFIELDER -> DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES;
            case CENTRE_MIDFIELDER -> DEFAULT_CENTRE_MIDFIELDER_TENDENCIES;
            case LEFT_MIDFIELDER -> DEFAULT_LEFT_MIDFIELDER_TENDENCIES;
            case RIGHT_MIDFIELDER -> DEFAULT_RIGHT_MIDFIELDER_TENDENCIES;
            case LEFT_WINGER -> DEFAULT_LEFT_WINGER_TENDENCIES;
            case OFFENSIVE_MIDFIELDER -> DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES;
            case RIGHT_WINGER -> DEFAULT_RIGHT_WINGER_TENDENCIES;
            case FORWARD -> DEFAULT_FORWARD_TENDENCIES;
            case AERIAL_FORWARD -> DEFAULT_AERIAL_FORWARD_TENDENCIES;
            case STRIKER -> DEFAULT_STRIKER_TENDENCIES;
            case AERIAL_STRIKER -> DEFAULT_AERIAL_STRIKER_TENDENCIES;
            case GOALKEEPER -> DEFAULT_GOALKEEPER_TENDENCIES;
        };
    }

    private String id;
    private PlayerPosition position;
    private Map<PlayerSkill, PlayerSkills> tendencies;

    public Boolean isDefault() {
        return DEFAULT_TENDENCIES.contains(this);
    }

    public Optional<PlayerSkill> getRandomSkillBasedOnTendency(Set<PlayerSkill> excludedSkills) {
        List<PlayerSkill> distributedSkills = getDistributedSkills(excludedSkills);

        return distributedSkills.isEmpty()
                ? Optional.empty()
                : Optional.of(distributedSkills.get(RandomUtils.nextInt(0, distributedSkills.size())));
    }

    /**
     * Returns list with the distribution of skills based on tendency value.
     * For example, having DEFAULT_FORWARD_TENDENCIES and no excluded skill, the resulting list will be:
     * [SCORE, SCORE, SCORE, SCORE, SCORE,
     * OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION, OFFENSIVE_POSITION,
     * BALL_CONTROL, BALL_CONTROL, BALL_CONTROL, BALL_CONTROL,
     * PASSING, PASSING,
     * CO, CO,
     * TACKLING,
     * DEFENSE_POSITION]
     * @return
     */
    private ArrayList<PlayerSkill> getDistributedSkills(Set<PlayerSkill> excludedSkills) {
        return tendencies.entrySet().stream()
                .filter(entry -> !excludedSkills.contains(entry.getKey()))
                .flatMap(entry -> IntStream.range(0, entry.getValue().getActual())
                        .mapToObj(i -> entry.getKey())
                )
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
