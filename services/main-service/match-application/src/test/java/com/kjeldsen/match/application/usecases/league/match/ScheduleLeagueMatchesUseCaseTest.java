package com.kjeldsen.match.application.usecases.league.match;

import com.github.javafaker.Faker;
import com.kjeldsen.match.application.usecases.CreateMatchUseCase;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.ScheduledMatch;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import com.kjeldsen.match.domain.schedulers.MatchScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduleLeagueMatchesUseCaseTest {

    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final LeagueWriteRepository mockedLeagueWriteRepository = Mockito.mock(LeagueWriteRepository.class);
    private final CreateMatchUseCase mockedCreateMatchUseCase = Mockito.mock(CreateMatchUseCase.class);
    private final MatchScheduler mockedMatchScheduler = Mockito.mock(MatchScheduler.class);
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase = new ScheduleLeagueMatchesUseCase(
        mockedGetLeagueUseCase, mockedLeagueWriteRepository, mockedCreateMatchUseCase, mockedMatchScheduler);


    @Test
    @DisplayName("Should schedule matches")
    void should_schedule_matches() {
        Faker faker = new Faker();
        LocalDateTime dateTime = LocalDateTime.now().withHour(15).withMinute(30).withSecond(0).withNano(0);
        List<ScheduledMatch> scheduled = List.of(
            new ScheduledMatch("home", "away", dateTime),
            new ScheduledMatch("home", "away", dateTime),
            new ScheduledMatch("home", "away", dateTime));

        when(mockedGetLeagueUseCase.get("leagueId")).thenReturn(League.builder().id(League.LeagueId.of("leagueId")).build());
        when(mockedCreateMatchUseCase.create("home", "away", dateTime, "leagueId"))
            .thenReturn(Match.builder().dateTime(dateTime).id(faker.name().toString()).build());

        scheduleLeagueMatchesUseCase.schedule(scheduled, "leagueId");
        verify(mockedCreateMatchUseCase, times(scheduled.size())).create(any(), any(), any(), any());
        verify(mockedMatchScheduler, times(scheduled.size())).scheduleMatch(any(), any());

    }
}