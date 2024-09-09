package com.kjeldsen.match.engine;

import com.github.javafaker.Faker;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerReceptionPreference;
import com.kjeldsen.player.domain.PlayerSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.kjeldsen.player.domain.PlayerStatus;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public final class RandomHelper {

    /*
     * Generate random resources for testing
     */

    static final Faker faker = new Faker();

    // Returns 11 random players including goalkeeper and a striker
    public static List<Player> genActivePlayers(Team team) {
        List<Player> players = new ArrayList<>(11);
        players.add(genActivePlayerWithPosition(team, PlayerPosition.RIGHT_BACK));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.CENTRE_BACK));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.LEFT_BACK));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.CENTRE_MIDFIELDER));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.FORWARD));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.FORWARD));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.STRIKER));
        players.add(genActivePlayerWithPosition(team, PlayerPosition.GOALKEEPER));

        double random = Math.random();
        if (random > 0.5) {
            players.add(genActivePlayerWithPosition(team, PlayerPosition.RIGHT_WINGER));
            players.add(genActivePlayerWithPosition(team, PlayerPosition.LEFT_WINGER));
        } else {
            players.add(genActivePlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER));
            players.add(genActivePlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER));
        }

        return players;
    }

    // Returns 6 random players including goalkeeper and a striker
    public static List<Player> genBenchPlayers(Team team) {
        List<Player> players = new ArrayList<>(6);
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.LEFT_BACK));
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.RIGHT_BACK));
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.LEFT_MIDFIELDER));
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.FORWARD));
        players.add(genBenchPlayerWithPosition(team, PlayerPosition.GOALKEEPER));
        return players;
    }

    public static Player genActivePlayerWithPosition(Team team, PlayerPosition position) {
        return genPlayerWithPosition(team, PlayerStatus.ACTIVE, position);
    }

    public static Player genBenchPlayerWithPosition(Team team, PlayerPosition position) {
        return genPlayerWithPosition(team, PlayerStatus.BENCH, position);
    }

    private static Player genPlayerWithPosition(Team team, PlayerStatus status, PlayerPosition position) {
        return Player.builder()
                .id(RandomStringUtils.random(5))
                .teamId(team.getId())
                .name(faker.name().fullName())
                .status(status)
                .position(position)
                .playerOrder(genPlayerOrder())
                .receptionPreference(genPlayerReceptionPreference())
                .skills(genSkillSet()).build();
    }

    public static Player genPlayer(Team team) {
        return Player.builder()
            .id(RandomStringUtils.random(5))
            .teamId(team.getId())
            .name(faker.name().fullName())
            .status(PlayerStatus.ACTIVE)
            .position(PlayerPosition.values()[new Random().nextInt(PlayerPosition.values().length)])
            .receptionPreference(genPlayerReceptionPreference())
            .skills(genSkillSet()).build();
    }

    private static Map<PlayerSkill, Integer> genSkillSet() {
        return Arrays.stream(PlayerSkill.values())
            .map(skill -> Map.entry(skill, genAttributeRating()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static PlayerOrder genPlayerOrder() {
        return PlayerOrder.values()[new Random().nextInt(PlayerOrder.values().length)];
    }

    private static PlayerReceptionPreference genPlayerReceptionPreference() {
        return PlayerReceptionPreference.values()[new Random().nextInt(PlayerReceptionPreference.values().length)];
    }

    public static int genAttributeRating() {
        return new Random().nextInt(30, 100);
    }

    public static Team genTeam() {
        Team team = Team.builder()
            .id(RandomStringUtils.random(5))
            .build();
        List<Player> players = genActivePlayers(team);
        List<Player> bench = genBenchPlayers(team);
        int rating = genAttributeRating();

        Tactic tactic = Tactic.DOUBLE_TEAM;
        VerticalPressure vp = VerticalPressure.MID_PRESSURE;
        HorizontalPressure hp = HorizontalPressure.SWARM_CENTRE;
//        Tactic tactic = Tactic.values()[new Random().nextInt(Tactic.values().length)];
//        VerticalPressure vp =
//            VerticalPressure.values()[new Random().nextInt(VerticalPressure.values().length)];
//        HorizontalPressure hp =
//            HorizontalPressure.values()[new Random().nextInt(HorizontalPressure.values().length)];

        return Team.builder()
            .id(team.getId())
            .players(players)
            .bench(bench)
            .tactic(tactic)
            .verticalPressure(vp)
            .horizontalPressure(hp)
            .rating(rating)
            .build();
    }
}
