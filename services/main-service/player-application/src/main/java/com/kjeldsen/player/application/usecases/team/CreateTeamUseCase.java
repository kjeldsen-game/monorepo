package com.kjeldsen.player.application.usecases.team;

import com.kjeldsen.lib.clients.LeagueClientApi;
import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueRequestClient;
import com.kjeldsen.lib.model.league.CreateOrAssignTeamToLeagueResponseClient;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Rating;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.factories.TeamFactory;
import com.kjeldsen.player.domain.provider.RatingProvider;
import com.kjeldsen.player.domain.provider.TeamProvider;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import com.kjeldsen.player.domain.utils.RatingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateTeamUseCase {

    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final TeamWriteRepository teamWriteRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final LeagueClientApi leagueClientApi;

    public void create(String teamName, Integer numberOfPlayers, String userId) {
        log.debug("CreateTeamUseCase name {} with {} players for user {}", teamName, numberOfPlayers, userId);

        Team team = TeamFactory.create(userId, teamName);
        teamWriteRepository.save(team);

        CreateOrAssignTeamToLeagueResponseClient response =
            leagueClientApi.assignTeamToLeague(CreateOrAssignTeamToLeagueRequestClient.builder()
                .teamId(team.getId().value()).teamName(teamName).build());

//        List<Player> players = generatePlayersUseCase.generateCustomPlayers(team.getId());

        List<Player> players = TeamProvider.provide(team.getId());
        Rating teamRating = RatingProvider.getTeamRating(players);
        // Generate team players
        players.forEach(playerWriteRepository::save);
        team.setRating(teamRating);
        team.setLeagueId(response.getLeagueId());
        teamWriteRepository.save(team);
    }
}
