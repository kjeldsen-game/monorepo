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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthDelegate implements AuthApiDelegate {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final CreateTeamUseCase createTeamUseCase;
    private final TeamReadRepository teamReadRepository;

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) {
            throw new RuntimeException("User not logged in");
        }

        String email = subject.getPrincipal().toString();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Team> team = teamReadRepository.findByUserId(user.getId());
        if (team.isEmpty()) {
            throw new RuntimeException("Team not found");
        }

        UserDetailsResponse details = new UserDetailsResponse();
        details.setEmail(user.getEmail());
        details.setId(user.getId());
        details.setTeamId(team.get().getId().value());

        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<String> login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
            .map(user -> {
                if (!passwordService.passwordsMatch(request.getPassword(), user.getPassword())) {
                    return ResponseEntity.badRequest().body("Password incorrect");
                }

                UsernamePasswordToken token =
                    new UsernamePasswordToken(user.getEmail(), user.getPassword());
                token.setRememberMe(true);
                SecurityUtils.getSubject().login(token);
                return ResponseEntity.ok("Login successful");
            })
            .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }

    @Override
    public ResponseEntity<String> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
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
            return ResponseEntity.badRequest().body("Team name already exists");
        }

        User registered = userRepository.save(user);
        createTeamUseCase.create(teamName, 50, registered.getId());
        return ResponseEntity.ok("User created");
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
}
