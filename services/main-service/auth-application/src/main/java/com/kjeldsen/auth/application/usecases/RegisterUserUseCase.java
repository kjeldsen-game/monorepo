package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.clients.TeamClientAuth;
import com.kjeldsen.auth.domain.clients.models.TeamDTO;
import com.kjeldsen.auth.domain.exceptions.TeamException;
import com.kjeldsen.auth.domain.exceptions.UsernameException;
import com.kjeldsen.auth.domain.publishers.UserRegisterPublisher;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.events.UserRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserReadRepository userReadRepository;
    private final UserWriteRepository userWriteRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamClientAuth teamClientAuth;
    private final UserRegisterPublisher userRegisterPublisher;

    public void register(String email, String password, String inputTeamName) {
        log.info("RegisterUserUseCase for email {} team {} password ****", email, inputTeamName);

        if (userReadRepository.findByEmail(email).isPresent()) {
            throw new UsernameException("Username taken!");
        }

        List<TeamDTO> teamDTOs = teamClientAuth.getTeam(inputTeamName, null);

        if (!teamDTOs.isEmpty()) {
            throw new TeamException("Team name taken!");
        }

        User user = new User();
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(Role.USER));
        User registered = userWriteRepository.save(user);
        userRegisterPublisher.publishUserRegisterEvent(
            UserRegisterEvent.builder()
                .id(EventId.generate())
                .occurredAt(Instant.now())
                .teamName(inputTeamName)
                .userId(registered.getId())
                .numberOfPlayers(50)
                .build());
    }
}
