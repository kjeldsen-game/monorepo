package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.application.usecases.GenerateTokenUseCase;
import com.kjeldsen.auth.application.usecases.GetUserUseCase;
import com.kjeldsen.auth.application.usecases.RegisterUserUseCase;
import com.kjeldsen.auth.authentication.api.AuthApiDelegate;
import com.kjeldsen.auth.authentication.model.RegisterRequest;
import com.kjeldsen.auth.authentication.model.TokenRequest;
import com.kjeldsen.auth.authentication.model.TokenResponse;
import com.kjeldsen.auth.authentication.model.UserDetailsResponse;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDelegate implements AuthApiDelegate {

    private final GetUserUseCase getUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final TeamClientApi teamClientApi;

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        User user = getUserUseCase.getCurrent();
        UserDetailsResponse details = buildUserDetailsResponse(user);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest request) {
        registerUserUseCase.register(request.getEmail(), request.getPassword(), request.getTeamName());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<TokenResponse> generateToken(TokenRequest tokenRequest) {
        String token = generateTokenUseCase.get(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse().accessToken(token));
    }

    private UserDetailsResponse buildUserDetailsResponse(User user) {
        List<TeamClient> clientResponse = teamClientApi.getTeam(null, null, user.getId());
        if (clientResponse.isEmpty()) {
            throw new TeamNotFoundException();
        }
        return new UserDetailsResponse(user.getId(), user.getEmail(), clientResponse.get(0).getId());
    }
}
