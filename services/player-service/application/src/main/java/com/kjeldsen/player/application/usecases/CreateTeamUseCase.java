package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamId;
import com.kjeldsen.player.domain.TeamName;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateTeamUseCase {
    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final TeamWriteRepository teamWriteRepository;

    public void create(String teamName, Integer numberOfPlayers, String userId) {
        log.info("Creating team {} with {} players", teamName, numberOfPlayers);

        TeamId newTeamId = TeamId.generate();
        List<Player> players = generatePlayersUseCase.generate(numberOfPlayers, newTeamId);
        Team team = Team.builder()
            .id(newTeamId)
            .name(TeamName.of(teamName))
            .players(players)
            .build();

        teamWriteRepository.save(team, userId);
    }
}
