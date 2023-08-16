package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.api.TeamApiDelegate;
import com.kjeldsen.player.rest.mapper.TeamMapper;
import com.kjeldsen.player.rest.model.TeamResponse;
import com.kjeldsen.security.AuthenticationFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TeamDelegate implements TeamApiDelegate {

    private final GetTeamUseCase getTeamUseCase;
    private final AuthenticationFetcher authenticationFetcher;

    @Override
    public ResponseEntity<TeamResponse> getTeam() {
        String loggedUserID = authenticationFetcher.getLoggedUserID();
        final Team team = getTeamUseCase.get(loggedUserID);
        return ResponseEntity.ok(TeamMapper.INSTANCE.map(team));
    }

}
