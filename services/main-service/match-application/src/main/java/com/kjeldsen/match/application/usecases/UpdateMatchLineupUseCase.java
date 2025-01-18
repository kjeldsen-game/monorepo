package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.TeamModifiers;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.repositories.MatchReadRepository;
import com.kjeldsen.match.repositories.MatchWriteRepository;
import com.kjeldsen.match.validation.TeamFormationValidationResult;
import com.kjeldsen.match.validation.TeamFormationValidator;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateMatchLineupUseCase {

    private final MatchReadRepository matchReadRepository;
    private final MatchWriteRepository matchWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void update(String matchId, String teamId, List<PlayerUpdateDTO> playerList, TeamModifiers teamModifiers) {
        log.info("UpdateMatchLineupUseCase for match={} team={}", matchId, teamId);
        Match match = matchReadRepository.findOneById(matchId).orElseThrow(
            () -> new RuntimeException("Match not found"));

        // Retrieve the desired Team
        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }

        TeamRole role = match.getHome().getId().equals(teamId)
            ? TeamRole.HOME : TeamRole.AWAY;

        List<Player> domainPlayers = playerList.stream()
            .flatMap(player -> playerReadRepository.findOneById(Player.PlayerId.of(player.getId()))
                .map(playerDomain -> {
                    playerDomain.setPosition(player.getPosition());
                    playerDomain.setStatus(player.getStatus());
                    playerDomain.setPlayerOrder(player.getPlayerOrder());
                    return Stream.of(playerDomain);
                })
                .orElse(Stream.empty()))
            .toList();

        List<com.kjeldsen.match.entities.Player> newBenchPlayers = domainPlayers.stream()
            .filter(domainPlayer -> domainPlayer.getStatus().equals(PlayerStatus.BENCH))
            .map(player -> buildPlayer(player, role))
            .toList();

        List<com.kjeldsen.match.entities.Player> newActivePlayers = domainPlayers.stream()
            .filter(domainPlayer -> domainPlayer.getStatus().equals(PlayerStatus.ACTIVE))
            .map(player -> buildPlayer(player, role))
            .toList();

        // Update players and modifiers for the match
        team.setPlayers(newActivePlayers);
        team.setBench(newBenchPlayers);
        team.setHorizontalPressure(teamModifiers.getHorizontalPressure());
        team.setVerticalPressure(teamModifiers.getVerticalPressure());
        team.setTactic(teamModifiers.getTactic());

        team.setSpecificLineup(true);
        matchWriteRepository.save(match);
    }

    private com.kjeldsen.match.entities.Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return com.kjeldsen.match.entities.Player.builder()
            .id(player.getId().value())
            .name(player.getName())
            .status(player.getStatus())
            .teamId(player.getTeamId().value())
            .teamRole(teamRole)
            .position(player.getPosition())
            .skills(skills)
            .playerOrder(player.getPlayerOrder())
            .build();
    }

    @Getter
    @Setter
    @Builder
    public static class PlayerUpdateDTO {
        private String id;
        private PlayerStatus status;
        private PlayerPosition position;
        private PlayerOrder playerOrder;
    }
}
