package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
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

        Team.TeamId newTeamId = Team.TeamId.generate();
        List<Player> players = generatePlayersUseCase.generate(numberOfPlayers, newTeamId);
        Team team = Team.builder()
            .id(newTeamId)
            .name(teamName)
            .players(players)
            .build();
// TODO apart from saving the team aggreagate/projection, we need to store a created_team_event. Then Team domain object should have a method like
//  create(created_team_event) and based on the event it populates itlsef. Then you save it with the repo
        teamWriteRepository.save(team, userId);
    }
}
