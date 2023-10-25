package com.kjeldsen.match.engine;

import com.github.javafaker.Faker;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.player.Player;
import com.kjeldsen.match.entities.player.PlayerPosition;
import com.kjeldsen.match.entities.player.PlayerSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public final class RandomHelper {

    /*
     * Generate random resources for testing
     */

    static final Faker faker = new Faker();

    // Returns 11 random players including goalkeeper and a striker
    public static List<Player> genPlayers(Id teamId) {
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

    public static Player genPlayerWithPosition(Id teamId, PlayerPosition position) {
        return Player.builder()
            .id(Id.generate())
            .teamId(teamId)
            .name(faker.name().fullName())
            .position(position)
            .skillSet(genSkillSet()).build();
    }

    public static Player genPlayer(Id teamId) {
        return Player.builder()
            .id(Id.generate())
            .teamId(teamId)
            .name(faker.name().fullName())
            .position(PlayerPosition.values()[new Random().nextInt(PlayerPosition.values().length)])
            .skillSet(genSkillSet()).build();
    }

    private static Map<PlayerSkill, Integer> genSkillSet() {
        return Arrays.stream(PlayerSkill.values())
            .map(skill -> Map.entry(skill, genAttributeRating()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static int genAttributeRating() {
        return new Random().nextInt(30, 100);
    }

    public static Team genTeam() {
        Id teamId = Id.generate();
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
