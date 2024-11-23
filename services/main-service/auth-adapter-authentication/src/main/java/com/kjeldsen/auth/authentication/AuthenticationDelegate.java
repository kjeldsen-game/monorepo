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
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDelegate implements AuthApiDelegate {

    private final TeamReadRepository teamReadRepository;
    private final GetUserUseCase getUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;

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
        Optional<Team> team = teamReadRepository.findByUserId(user.getId());
        if (team.isEmpty()) {
            throw new RuntimeException("Team not found");
        }

        UserDetailsResponse details = new UserDetailsResponse();
        details.setEmail(user.getEmail());
        details.setId(user.getId());
        details.setTeamId(team.get().getId().value());
        return details;
    }
}
