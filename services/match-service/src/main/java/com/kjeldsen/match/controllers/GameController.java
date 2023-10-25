package com.kjeldsen.match.controllers;

import com.github.javafaker.Faker;
import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Play;
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
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    /*
     * Ignore - this is for mocking up games for feedback
     */

    @GetMapping("/create")
    public ResponseEntity<?> create() {

        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        Match match = Match.builder()
            .id(Id.generate())
            .home(home)
            .away(away)
            .build();

        GameState state = Game.play(match);
        GameDTO game = GameDTO.builder()
            .home(home)
            .away(away)
            .plays(state.getPlays())
            .homeScore(state.getHome().getScore())
            .awayScore(state.getAway().getScore())
            .build();

        return ResponseEntity.ok(game);
    }

    @Builder
    private record GameDTO(
        Team home, Team away, List<Play> plays, int homeScore, int awayScore) {

    }

    private static class RandomHelper {

        static final Faker faker = new Faker();

        private static Team genTeam() {
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

        private static int genAttributeRating() {
            return new Random().nextInt(1, 100);
        }

        private static List<Player> genPlayers(Id teamId) {
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
            players.add(genPlayerWithPosition(teamId, PlayerPosition.FORWARD));
            players.add(genPlayerWithPosition(teamId, PlayerPosition.GOALKEEPER));
            return players;
        }

        private static Player genPlayerWithPosition(Id teamId, PlayerPosition position) {
            return Player.builder()
                .id(Id.generate())
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
    }
}
