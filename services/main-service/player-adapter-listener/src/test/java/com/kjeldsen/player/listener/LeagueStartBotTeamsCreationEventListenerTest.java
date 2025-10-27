package com.kjeldsen.player.listener;

import com.kjeldsen.lib.events.LeagueStartBotTeamsCreationEvent;
import com.kjeldsen.player.application.usecases.team.CreateBotTeamsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeagueStartBotTeamsCreationEventListenerTest {

    @Mock
    private CreateBotTeamsUseCase mockedCreateBotTeamsUseCase;
    @InjectMocks
    private LeagueStartBotTeamsCreationEventListener leagueStartBotTeamsCreationEventListener;

    @Test
    @DisplayName("Should handle event and process")
    void should_handle_event_and_process() {
        var event = LeagueStartBotTeamsCreationEvent.builder().leagueId("ll").count(8).build();
        leagueStartBotTeamsCreationEventListener.handleAuctionEndEvent(event);
        Mockito.verify(mockedCreateBotTeamsUseCase, Mockito.times(1)).create(8, "ll");
    }
}