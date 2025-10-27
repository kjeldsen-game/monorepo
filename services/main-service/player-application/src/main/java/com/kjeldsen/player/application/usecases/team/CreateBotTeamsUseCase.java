package com.kjeldsen.player.application.usecases.team;

import com.github.javafaker.Faker;
import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.factories.TeamFactory;
import com.kjeldsen.player.domain.provider.DefaultLineupProvider;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateBotTeamsUseCase {

    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final TeamWriteRepository teamWriteRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final GenericEventPublisher genericEventPublisher;
    private final Faker faker = new Faker();

    public void create(int botCount, String leagueId) {
        log.info("CreateBotTeamsUseCae for leagueId = {} botCount = {}", leagueId, botCount);
        Map<String, String> teams = new HashMap<>();

        if (leagueId == null) {
            throw new IllegalArgumentException("LeagueId cannot be null");
        }

        IntStream.range(0, botCount).forEach(index -> {
            Team team = TeamFactory.create(null, faker.country().name() + " " + faker.team().name());
            team.setLeagueId(leagueId);
            teamWriteRepository.save(team);
            List<Player> players = generatePlayersUseCase.generateCustomPlayers(team.getId());
            DefaultLineupProvider.set(players);
            players.forEach(playerWriteRepository::save);
            teams.put(team.getId().value(), team.getName());
        });

        genericEventPublisher.publishEvent(TeamCreationEvent
            .builder().isBots(true).teams(teams).leagueId(leagueId).build());
    }
}
