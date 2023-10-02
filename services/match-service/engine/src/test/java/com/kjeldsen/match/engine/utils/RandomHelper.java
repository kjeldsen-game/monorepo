package com.kjeldsen.match.engine.utils;

import com.github.javafaker.Faker;
import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.PlayerPosition;
import com.kjeldsen.match.domain.type.PlayerSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RandomHelper {

    /*
     * Generate random resources for testing
     */

    static final Faker faker = new Faker();

    public static Player genPlayer(TeamId teamId) {
        int index = new Random().nextInt(0, PlayerPosition.values().length - 1);
        return Player.builder()
            .id(PlayerId.generate())
            .teamId(teamId)
            .name(faker.name().fullName())
            .position(PlayerPosition.values()[index])
            .skillSet(genSkillSet()).build();
    }

    // Returns 11 random players including goalkeeper and a striker
    public static List<Player> genPlayers(TeamId teamId) {
        List<Player> players = new ArrayList<>(11);
        players.add(genPlayerWithPosition(teamId, PlayerPosition.LEFT_BACK));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.RIGHT_BACK));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.CENTER_BACK));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.LEFT_MIDFIELDER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.CENTER_MIDFIELDER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.LEFT_WINGER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.RIGHT_WINGER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.STRIKER));
        players.add(genPlayerWithPosition(teamId, PlayerPosition.GOALKEEPER));
        return players;
    }

    public static Player genPlayerWithPosition(TeamId teamId, PlayerPosition position) {
        return Player.builder()
            .id(PlayerId.generate())
            .teamId(teamId)
            .name(faker.name().fullName())
            .position(position)
            .skillSet(genSkillSet()).build();
    }

    private static Map<PlayerSkill, Integer> genSkillSet() {
        return Arrays.stream(PlayerSkill.values())
            .map(skill -> Map.entry(skill, genAttributeRating()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String genString(int size) {
        return new Random()
            .ints(size, 'a', 'z' + 1)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public static int genAttributeRating() {
        return new Random().nextInt(0, 100);
    }

    public static Team genTeam() {
        TeamId teamId = TeamId.generate();
        List<Player> players = genPlayers(teamId);
        List<Player> bench = genPlayers(teamId);
        int rating = genAttributeRating();
        return Team.builder()
            .id(teamId)
            .players(players)
            .bench(bench)
            .rating(rating)
            .build();
    }
}
