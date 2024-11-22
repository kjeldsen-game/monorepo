package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserReadRepository userReadRepository;
    private final UserWriteRepository userWriteRepository;
    private final TeamReadRepository teamReadRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateTeamUseCase createTeamUseCase;

    public void register(String email, String password, String inputTeamName) {
        log.info("RegisterUserUseCase for email {} team {} password ****", email, inputTeamName);
        if (userReadRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Username taken");
        }

        Optional<Team> team = teamReadRepository.findByTeamName(inputTeamName);
        if (team.isPresent()) {
            throw new RuntimeException("Team name taken");
        }

        User user = new User();
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(Role.USER));
        User registered = userWriteRepository.save(user);
        createTeamUseCase.create(inputTeamName, 50, registered.getId());
    }
}
