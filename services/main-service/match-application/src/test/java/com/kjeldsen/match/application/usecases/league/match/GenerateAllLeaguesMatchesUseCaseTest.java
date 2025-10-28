package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.ScheduledMatch;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class GenerateAllLeaguesMatchesUseCaseTest {

    private final LeagueReadRepository mockedLeagueReadRepository = Mockito.mock(LeagueReadRepository.class);
    private final GenerateScheduledMatchesUseCase mockedGenerateScheduledMatchesUseCase = Mockito.mock(GenerateScheduledMatchesUseCase.class);
    private final ScheduleLeagueMatchesUseCase mockedScheduleLeagueMatchesUseCase = Mockito.mock(ScheduleLeagueMatchesUseCase.class);
    private final GenericEventPublisher mockedGenericEventPublisher = Mockito.mock(GenericEventPublisher.class);
    private final GenerateAllLeaguesMatchesUseCase generateAllLeaguesMatchesUseCase = new GenerateAllLeaguesMatchesUseCase(
        mockedLeagueReadRepository, mockedGenerateScheduledMatchesUseCase, mockedScheduleLeagueMatchesUseCase, mockedGenericEventPublisher);

    @Test
    @DisplayName("Should generate matches, schedule and publish event")
    void should_generate_matches_and_publish_event() {
        when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.PRESEASON)).thenReturn(List.of(
            League.builder().id(League.LeagueId.of("league1")).status(League.LeagueStatus.PRESEASON).teams(
                Map.of("team1", new League.LeagueStats())
            ).build()
        ));
        generateAllLeaguesMatchesUseCase.generate();
        verify(mockedGenerateScheduledMatchesUseCase, times(1))
            .generate("league1", false);
        verify(mockedScheduleLeagueMatchesUseCase, times(1)).schedule(any(List.class), anyString());
        verify(mockedGenericEventPublisher, times(1)).publishEvent(any(NotificationEvent.class));
    }
}