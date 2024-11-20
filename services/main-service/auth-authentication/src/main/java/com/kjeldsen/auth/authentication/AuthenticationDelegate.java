package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.AuthService;
import com.kjeldsen.auth.Role;
import com.kjeldsen.auth.User;
import com.kjeldsen.auth.UserRepository;
import com.kjeldsen.auth.authentication.api.AuthApiDelegate;
import com.kjeldsen.auth.authentication.model.RegisterRequest;
import com.kjeldsen.auth.authentication.model.TokenRequest;
import com.kjeldsen.auth.authentication.model.TokenResponse;
import com.kjeldsen.auth.authentication.model.UserDetailsResponse;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDelegate implements AuthApiDelegate {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateTeamUseCase createTeamUseCase;
    private final TeamReadRepository teamReadRepository;
    private final AuthService authService;

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        User user = authService.currentUser()
            .orElseThrow(() -> new RuntimeException("User not logged in"));

        UserDetailsResponse details = buildUserDetailsResponse(user);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(Role.USER));

        String teamName = request.getTeamName();
        FindTeamsQuery query = FindTeamsQuery.builder()
            .name(teamName)
            .page(1)
            .size(1)
            .build();
        if (!teamReadRepository.find(query).isEmpty()) {
            throw new RuntimeException("Team name taken");
        }

        User registered = userRepository.save(user);
        createTeamUseCase.create(teamName, 50, registered.getId());
        return ResponseEntity.ok().build();
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

    @Override
    public ResponseEntity<TokenResponse> generateToken(TokenRequest tokenRequest) {

        Optional<User> userOpt = authService.findByEmail(tokenRequest.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("user not found");
        }

        if (!passwordEncoder.matches(tokenRequest.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("invalid password");
        }

        return ResponseEntity.ok(
            new TokenResponse()
                .accessToken(jwtTokenProvider.generateToken(userOpt.get().getId(), userOpt.get().getRoles())));
    }

}
