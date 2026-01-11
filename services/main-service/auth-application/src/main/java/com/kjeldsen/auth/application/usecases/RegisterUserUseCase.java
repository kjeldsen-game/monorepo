package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.models.Role;
import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.auth.domain.utils.PasswordValidator;
import com.kjeldsen.lib.TeamClientApiImpl;
import com.kjeldsen.lib.events.UserRegisterEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserReadRepository userReadRepository;
    private final UserWriteRepository userWriteRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamClientApiImpl teamClientApi;
    private final GenericEventPublisher userRegisterPublisher;

    public void register(String email, String password, String inputTeamName, String confirmPassword) {
        log.info("RegisterUserUseCase for email {} team {} password ****", email, inputTeamName);

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BadRequestException("Invalid email address format!");
        }

        PasswordValidator.validatePassword(password, confirmPassword);

        if (userReadRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email taken!");
        }

        List<TeamResponse> clientResponse = teamClientApi.getTeams( inputTeamName, null);
        if (!clientResponse.isEmpty()) {
            throw new BadRequestException("Team name taken!");
        }

        User user = User.builder().build();
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        String teamId = java.util.UUID.randomUUID().toString();
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(Role.USER));
        user.setTeamId(teamId);
        User registered = userWriteRepository.save(user);
        userRegisterPublisher.publishEvent(
            UserRegisterEvent.builder()
                .teamId(user.getTeamId())
                .teamName(inputTeamName)
                .userId(registered.getId())
                .numberOfPlayers(50)
                .build());
    }
}
