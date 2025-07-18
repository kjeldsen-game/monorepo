package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
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

        List<TeamClient> teamClientList = teamClientApi.getTeam(null, null, user.getId());

        if (teamClientList.isEmpty()) {
            throw new NotFoundException("Team not found for user " + user.getId());
        }

        TeamClient teamClient = teamClientList.get(0);

        return Profile.builder().email(user.getEmail()).teamName(teamClient.getName())
            .avatar(user.convertBytesToString()).build();
    }
}
