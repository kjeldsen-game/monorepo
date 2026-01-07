package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class GetProfileUseCase {

    private final GetUserUseCase getUserUseCase;
    private final TeamClientApi teamClientApi;

    public Profile get() {
        User user = getUserUseCase.getCurrent();
        log.info("GetProfileUseCase for user {}", user.getEmail());

        TeamResponse teamResponse = teamClientApi.getTeamById(user.getTeamId());

        if (teamResponse == null) {
            throw new NotFoundException("Team not found for team " + user.getTeamId());
        }

        return Profile.builder().email(user.getEmail()).teamName(teamResponse.getName())
            .avatar(user.convertBytesToString()).build();
    }
}
