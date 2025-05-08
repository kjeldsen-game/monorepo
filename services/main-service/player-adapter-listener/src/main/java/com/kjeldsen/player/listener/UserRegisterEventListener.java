package com.kjeldsen.player.listener;


import com.kjeldsen.lib.events.UserRegisterEvent;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisterEventListener {

    private final CreateTeamUseCase createTeamUseCase;

    @EventListener
    public void handleRegisterUserEvent(UserRegisterEvent userRegisterEvent) {
        createTeamUseCase.create(userRegisterEvent.getTeamName(), userRegisterEvent.getNumberOfPlayers(),
            userRegisterEvent.getUserId());
    }
}
