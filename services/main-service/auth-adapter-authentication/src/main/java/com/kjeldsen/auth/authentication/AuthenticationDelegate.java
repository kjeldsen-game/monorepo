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
import com.kjeldsen.auth.domain.clients.TeamClientAuth;
import com.kjeldsen.auth.domain.clients.models.TeamDTO;
import com.kjeldsen.auth.domain.exceptions.TeamNotFoundException;
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
    private final TeamClientAuth teamClientAuth;

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
        List<TeamDTO> teamDTOs = teamClientAuth.getTeam(null, user.getId());
        if (teamDTOs.isEmpty()) {
            throw new TeamNotFoundException();
        }
        return new UserDetailsResponse(user.getId(), user.getEmail(), teamDTOs.get(0).getId());
    }
}
