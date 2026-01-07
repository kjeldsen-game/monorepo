package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.rest.model.PlayerStatus;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateMatchLineupUseCase {

    private final MatchWriteRepository matchWriteRepository;
    private final GetMatchTeamUseCase getMatchTeamUseCase;

    public void update(String matchId, String teamId, List<Player> playerList, TeamModifiers teamModifiers) {

        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(matchId, teamId);

        List<Player> newBenchPlayers = filterPlayersByStatus(
            PlayerStatus.BENCH, playerList);
        List<Player> newActivePlayers = filterPlayersByStatus(
            PlayerStatus.ACTIVE, playerList);

        matchAndTeam.team().setPlayers(newActivePlayers);
        matchAndTeam.team().setBench(newBenchPlayers);
        matchAndTeam.team().setModifiers(teamModifiers);
        matchAndTeam.team().setSpecificLineup(true);
        matchWriteRepository.save(matchAndTeam.match());
    }

    public void updateSelf(Match match, List<Player> playerList, TeamModifiers teamModifiers) {
        log.info("UpdateMatchLineupUseCase for match={} for the self clone team", match.getId());

        List<Player> newBenchPlayers = filterPlayersByStatus(
            PlayerStatus.BENCH, playerList);
        List<Player> newActivePlayers = filterPlayersByStatus(
            PlayerStatus.ACTIVE, playerList);

        match.getAway().setPlayers(newActivePlayers);
        match.getAway().setBench(newBenchPlayers);
        match.getAway().setModifiers(teamModifiers);

        match.getAway().setSpecificLineup(true);
        matchWriteRepository.save(match);
    }


    private List<com.kjeldsen.match.domain.entities.Player> filterPlayersByStatus(PlayerStatus status, List<Player> players) {
        return players.stream()
            .filter(domainPlayer ->
                 domainPlayer.getStatus().name().equals(status.getValue())).toList();
    }

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
}
