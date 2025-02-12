package com.kjeldsen.match.application.usecases;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.match.domain.clients.PlayerClientMatch;
import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;

import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateMatchLineupUseCase {

    private final MatchWriteRepository matchWriteRepository;
    private final GetMatchTeamUseCase getMatchTeamUseCase;
    private final PlayerClientMatch playerClient;

    public void update(String matchId, String teamId, List<PlayerUpdateDTO> playerList, TeamModifiers teamModifiers) {
        log.info("UpdateMatchLineupUseCase for match={} team={}", matchId, teamId);

        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(matchId, teamId);

        List<PlayerDTO> players = playerClient.getPlayers(teamId, SecurityUtils.getCurrentUserToken());
        playerList.forEach(player -> {
            Optional<PlayerDTO> matchingPlayerDTO = players.stream()
                .filter(playerDTO -> playerDTO.getId().equals(player.getId()))
                .findFirst();
            matchingPlayerDTO.ifPresent(playerDTO -> {
                playerDTO.setPosition(player.getPosition().name());
                playerDTO.setStatus(player.getStatus().name());
                playerDTO.setPlayerOrder(player.getPlayerOrder().name());
                if (player.getPlayerOrderDestinationPitchArea() != null) {
                    playerDTO.setPlayerOrderDestinationPitchArea(player.getPlayerOrderDestinationPitchArea().name());
                }
            });
        });

        List<com.kjeldsen.match.domain.entities.Player> newBenchPlayers = filterPlayersByStatus(
            PlayerStatus.BENCH, players, matchAndTeam.teamRole());
        List<com.kjeldsen.match.domain.entities.Player> newActivePlayers = filterPlayersByStatus(
            PlayerStatus.ACTIVE, players, matchAndTeam.teamRole());

        // Update players and modifiers for the match
        matchAndTeam.team().setPlayers(newActivePlayers);
        matchAndTeam.team().setBench(newBenchPlayers);
        matchAndTeam.team().setHorizontalPressure(teamModifiers.getHorizontalPressure());
        matchAndTeam.team().setVerticalPressure(teamModifiers.getVerticalPressure());
        matchAndTeam.team().setTactic(teamModifiers.getTactic());

        matchAndTeam.team().setSpecificLineup(true);
        matchWriteRepository.save(matchAndTeam.match());
    }

    private List<com.kjeldsen.match.domain.entities.Player> filterPlayersByStatus(PlayerStatus status, List<PlayerDTO> players, TeamRole role) {
        return players.stream()
            .filter(domainPlayer -> domainPlayer.getStatus().equals(status.name()))
            .map(player ->  buildPlayer(player, role))
            .toList();
    }

    // TODO extract outside of the class
    public static com.kjeldsen.match.domain.entities.Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return com.kjeldsen.match.domain.entities.Player.builder()
            .id(player.getId().value())
            .name(player.getName())
            .status(player.getStatus())
            .teamId(player.getTeamId().value())
            .teamRole(teamRole)
            .position(player.getPosition())
            .playerOrderDestinationPitchArea(player.getPlayerOrderDestinationPitchArea() != null ? player.getPlayerOrderDestinationPitchArea() : null)
            .skills(skills)
            .playerOrder(player.getPlayerOrder())
            .build();
    }

    public static com.kjeldsen.match.domain.entities.Player buildPlayer(PlayerDTO player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> PlayerSkill.valueOf(entry.getKey()),
                entry -> entry.getValue().getActual()
            ));skills.put(PlayerSkill.INTERCEPTING, 0);
        System.out.println(player.getPlayerOrderDestinationPitchArea());
        return com.kjeldsen.match.domain.entities.Player.builder()
            .id(player.getId())
            .name(player.getName())
            .status(PlayerStatus.valueOf(player.getStatus()))
            .teamId(player.getTeamId())
            .teamRole(teamRole)
            .playerOrderDestinationPitchArea(player.getPlayerOrderDestinationPitchArea() != null ? PitchArea.valueOf(player.getPlayerOrderDestinationPitchArea()) : null)
            .position(PlayerPosition.valueOf(player.getPosition()))
            .skills(skills)
            .playerOrder(PlayerOrder.valueOf(player.getPlayerOrder()))
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
        private PitchArea playerOrderDestinationPitchArea;
    }
}
