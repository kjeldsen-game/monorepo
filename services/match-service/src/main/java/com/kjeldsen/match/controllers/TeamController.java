package com.kjeldsen.match.controllers;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.github.javafaker.Faker;
import com.kjeldsen.match.security.AuthService;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.PlayerOrder;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.PlayerRepository;
import com.kjeldsen.match.repositories.TeamRepository;
import com.kjeldsen.match.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TeamController {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final AuthService authService;

    @PostMapping("/teams")
    public ResponseEntity<?> create(@RequestBody Team team, Authentication auth) {
        User user = authService.getUser(auth);
        if (user.getTeam() != null) {
            throw new ValidationException("You already have a team.");
        }
        validateTeam(team);

        // Defaults
        team.setUser(user);
        team.setRating(0);
        Team savedTeam = teamRepository.save(team);

        Faker faker = new Faker();
        List<Player> players = team.getPlayers().stream()
            .map(player ->
                Player.builder()
                    .name("%s %s".formatted(faker.name().firstName(), faker.name().lastName()))
                    .position(player.getPosition())
                    .skills(randomSkillSet())
                    .playerOrder(PlayerOrder.NONE)
                    .team(savedTeam)
                    .build())
            .toList();

        playerRepository.saveAll(players);
        savedTeam.setPlayers(players);
        return ok(savedTeam);
    }


    @GetMapping("/team/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        return teamRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(notFound().build());
    }

    @PatchMapping("/teams/{id}")
    public ResponseEntity<?> update(
        @PathVariable Long id, @RequestBody Team newTeam, Authentication auth) {

        User user = authService.getUser(auth);
        return teamRepository.findById(id)
            .map(existingTeam -> {
                if (!Objects.equals(existingTeam.getUser().getId(), user.getId())) {
                    throw new ValidationException("You do not own this team");
                }
                return existingTeam;
            })
            .map(existingTeam -> {
                if (newTeam.getTactic() != null) {
                    existingTeam.setTactic(newTeam.getTactic());
                }
                if (newTeam.getHorizontalPressure() != null) {
                    existingTeam.setHorizontalPressure(newTeam.getHorizontalPressure());
                }
                if (newTeam.getVerticalPressure() != null) {
                    existingTeam.setVerticalPressure(newTeam.getVerticalPressure());
                }
                return ok(teamRepository.save(existingTeam));
            })
            .orElse(notFound().build());
    }

    @GetMapping("/teams/random")
    public ResponseEntity<?> random() {
        Faker faker = new Faker();
        User newUser = User.builder()
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .build();

        User savedUser = userRepository.save(newUser);
        Tactic tactic = Tactic.values()[RandomUtils.nextInt(0, Tactic.values().length)];
        VerticalPressure vp =
            VerticalPressure.values()[RandomUtils.nextInt(0, VerticalPressure.values().length)];
        HorizontalPressure hp =
            HorizontalPressure.values()[RandomUtils.nextInt(0, HorizontalPressure.values().length)];
        Team newTeam = Team.builder()
            .user(savedUser)
            .rating(0)
            .tactic(tactic)
            .verticalPressure(vp)
            .horizontalPressure(hp)
            .build();

        Team savedTeam = teamRepository.save(newTeam);
        List<Player> newPlayers = randomPlayers(savedTeam);
        List<Player> savedPlayers = playerRepository.saveAll(newPlayers);
        savedTeam.setPlayers(savedPlayers);
        return ok(savedTeam);
    }

    private void validateTeam(Team team) {
        if (team.getPlayers().size() != 11) {
            throw new ValidationException("Team must have 11 players");
        }

        if (team.getPlayers().stream().noneMatch(player -> player.getPosition().isForward())) {
            throw new ValidationException("Team must have at least one forward player");
        }
    }

    private static List<Player> randomPlayers(Team team) {
        Faker faker = new Faker();
        List<Player> players = new ArrayList<>(11);
        Player goalkeeper = Player.builder()
            .team(team)
            .name("%s %s".formatted(faker.name().firstName(), faker.name().lastName()))
            .position(PlayerPosition.GOALKEEPER)
            .skills(randomSkillSet())
            .playerOrder(PlayerOrder.NONE)
            .build();

        players.add(goalkeeper);
        players.add(randomPlayer(team, PlayerPosition.LEFT_BACK));
        players.add(randomPlayer(team, PlayerPosition.RIGHT_BACK));
        players.add(randomPlayer(team, PlayerPosition.CENTER_BACK));
        players.add(randomPlayer(team, PlayerPosition.LEFT_MIDFIELDER));
        players.add(randomPlayer(team, PlayerPosition.RIGHT_MIDFIELDER));
        players.add(randomPlayer(team, PlayerPosition.CENTER_MIDFIELDER));
        players.add(randomPlayer(team, PlayerPosition.LEFT_WINGER));
        players.add(randomPlayer(team, PlayerPosition.RIGHT_WINGER));
        players.add(randomPlayer(team, PlayerPosition.STRIKER));
        players.add(randomPlayer(team, PlayerPosition.FORWARD));
        return players;
    }

    private static Player randomPlayer(Team team, PlayerPosition position) {
        Faker faker = new Faker();
        return Player.builder()
            .team(team)
            .name("%s %s".formatted(faker.name().firstName(), faker.name().lastName()))
            .position(position)
            .skills(randomSkillSet())
            .playerOrder(PlayerOrder.NONE)
            .build();
    }

    private static Map<SkillType, Integer> randomSkillSet() {
        return Arrays.stream(SkillType.values())
            .map(skill ->
                Map.entry(
                    skill,
                    new Random().nextInt(Player.MIN_SKILL_VALUE, Player.MAX_SKILL_VALUE)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
