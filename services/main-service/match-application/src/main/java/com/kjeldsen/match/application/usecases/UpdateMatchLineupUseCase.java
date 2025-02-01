package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
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

    private final MatchWriteRepository matchWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final GetMatchTeamUseCase getMatchTeamUseCase;

    public void update(String matchId, String teamId, List<PlayerUpdateDTO> playerList, TeamModifiers teamModifiers) {
        log.info("UpdateMatchLineupUseCase for match={} team={}", matchId, teamId);

        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(matchId, teamId);

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

        List<com.kjeldsen.match.domain.entities.Player> newBenchPlayers = domainPlayers.stream()
            .filter(domainPlayer -> domainPlayer.getStatus().equals(PlayerStatus.BENCH))
            .map(player -> buildPlayer(player, matchAndTeam.teamRole()))
            .toList();

        List<com.kjeldsen.match.domain.entities.Player> newActivePlayers = domainPlayers.stream()
            .filter(domainPlayer -> domainPlayer.getStatus().equals(PlayerStatus.ACTIVE))
            .map(player -> buildPlayer(player, matchAndTeam.teamRole()))
            .toList();

        // Update players and modifiers for the match
        matchAndTeam.team().setPlayers(newActivePlayers);
        matchAndTeam.team().setBench(newBenchPlayers);
        matchAndTeam.team().setHorizontalPressure(teamModifiers.getHorizontalPressure());
        matchAndTeam.team().setVerticalPressure(teamModifiers.getVerticalPressure());
        matchAndTeam.team().setTactic(teamModifiers.getTactic());

        matchAndTeam.team().setSpecificLineup(true);
        matchWriteRepository.save(matchAndTeam.match());
    }

    // TODO switch to private when removed from Delegate
    public com.kjeldsen.match.domain.entities.Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
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
