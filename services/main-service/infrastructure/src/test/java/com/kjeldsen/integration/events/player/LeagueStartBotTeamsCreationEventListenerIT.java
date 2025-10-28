package com.kjeldsen.integration.events.player;

import com.kjeldsen.integration.events.AbstractEventIT;
import com.kjeldsen.lib.events.LeagueStartBotTeamsCreationEvent;
import com.kjeldsen.player.application.usecases.team.CreateBotTeamsUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class LeagueStartBotTeamsCreationEventListenerIT extends AbstractEventIT {

    @MockitoBean
    private CreateBotTeamsUseCase mockedCreateBotTeamsUseCase;

    @Test
    void createBotTeamsUseCase() {
        testEventPublisher.publishEvent(LeagueStartBotTeamsCreationEvent.builder().leagueId("leagueId").count(10)
            .build());

        Mockito.verify(mockedCreateBotTeamsUseCase,
            Mockito.times(1)).create(10, "leagueId");
    }
}
