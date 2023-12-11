package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.rest.api.TeamApiDelegate;
import com.kjeldsen.player.rest.mapper.TeamMapper;
import com.kjeldsen.player.rest.model.EditPlayerRequest;
import com.kjeldsen.player.rest.model.EditTeamRequest;
import com.kjeldsen.player.rest.model.TeamResponse;
import com.kjeldsen.security.AuthenticationFetcher;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TeamDelegate implements TeamApiDelegate {

    private final GetTeamUseCase getTeamUseCase;
    private final AuthenticationFetcher authenticationFetcher;
    private final TeamReadRepository teamReadRepository;

    @Override
    public ResponseEntity<TeamResponse> getTeam() {
        String loggedUserID = authenticationFetcher.getLoggedUserID();
        final Team team = getTeamUseCase.get(loggedUserID);
        return ResponseEntity.ok(TeamMapper.INSTANCE.map(team));
    }

    @Override
    public ResponseEntity<List<TeamResponse>> getAllTeams(String name, Integer size, Integer page) {
        FindTeamsQuery query = FindTeamsQuery.builder()
            .name(name)
            .size(size)
            .page(page)
            .build();
        List<Team> teams = teamReadRepository.find(query);
        List<TeamResponse> response = teams.stream().map(TeamMapper.INSTANCE::map).toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TeamResponse> getTeamById(String teamId) {
        Team team = teamReadRepository.findOneById(Team.TeamId.of(teamId))
            .orElseThrow();
        TeamResponse response = TeamMapper.INSTANCE.map(team);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TeamResponse> updateTeamById(String teamId, EditTeamRequest editTeamRequest) {
        Team team = teamReadRepository.findOneById(Team.TeamId.of(teamId))
            .orElseThrow();

        List<EditPlayerRequest> players = editTeamRequest.getPlayers();
        // TODO validate positions
        // If validation passes then save all the players and the team

        TeamResponse response = TeamMapper.INSTANCE.map(team);
        return ResponseEntity.ok(response);
    }
}
