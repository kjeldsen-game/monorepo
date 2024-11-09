package com.kjeldsen.auth;

import com.kjeldsen.auth.rest.api.AuthApiDelegate;
import com.kjeldsen.auth.rest.model.LoginRequest;
import com.kjeldsen.auth.rest.model.RegisterRequest;
import com.kjeldsen.auth.rest.model.UserDetailsResponse;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthDelegate implements AuthApiDelegate {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final CreateTeamUseCase createTeamUseCase;
    private final TeamReadRepository teamReadRepository;
    private final AuthService authService;

    @Override
    public ResponseEntity<Void> unauthorized() {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        User user = authService.currentUser()
            .orElseThrow(() -> new RuntimeException("User not logged in"));

        UserDetailsResponse details = buildUserDetailsResponse(user);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<UserDetailsResponse> login(LoginRequest request) {
        // Remembers the user's token so they don't need to log in every time. This is set to true
        // by default but the login screen can add a checkbox to allow users to disable it.
        log.info("Executing login request from request: {}", request.getEmail());
        boolean rememberMe = true;
        return userRepository.findByEmail(request.getEmail())
            .map(user -> {
                if (!passwordService.passwordsMatch(request.getPassword(), user.getPassword())) {
                    throw new RuntimeException("Password incorrect");
                }

                UsernamePasswordToken token =
                    new UsernamePasswordToken(user.getEmail(), user.getPassword());
                System.out.println(token);
                token.setRememberMe(rememberMe);
                SecurityUtils.getSubject().login(token);

                UserDetailsResponse details = buildUserDetailsResponse(user);
                return ResponseEntity.ok(details);
            })
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        String hash = passwordService.encryptPassword(request.getPassword());
        user.setPassword(hash);

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

    @Override
    public ResponseEntity<String> logout() {
        Subject user = SecurityUtils.getSubject();
        if (user == null) {
            return ResponseEntity.badRequest().body("User not logged in");
        }
        user.logout();
        return ResponseEntity.ok("Logout successful");
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
