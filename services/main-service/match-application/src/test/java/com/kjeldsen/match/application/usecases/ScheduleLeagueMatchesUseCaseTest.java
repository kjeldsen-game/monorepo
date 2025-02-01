package com.kjeldsen.match.application.usecases;

import com.github.javafaker.Faker;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.schedulers.MatchScheduler;
import org.hibernate.sql.ast.tree.expression.Over;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduleLeagueMatchesUseCaseTest {

    private final CreateMatchUseCase mockedCreateMatchUseCase = Mockito.mock(CreateMatchUseCase.class);
    private final MatchScheduler mockedMatchScheduler = Mockito.mock(MatchScheduler.class);
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase = new ScheduleLeagueMatchesUseCase(
        mockedCreateMatchUseCase, mockedMatchScheduler);


    @Test
    @DisplayName("Should schedule matches")
    void should_schedule_matches() {
        Faker faker = new Faker();
        LocalDateTime dateTime = LocalDateTime.now().withHour(15).withMinute(30).withSecond(0).withNano(0);
        List<GenerateMatchScheduleUseCase.ScheduledMatch> scheduled = List.of(
            new GenerateMatchScheduleUseCase.ScheduledMatch("home", "away", dateTime),
            new GenerateMatchScheduleUseCase.ScheduledMatch("home", "away", dateTime),
            new GenerateMatchScheduleUseCase.ScheduledMatch("home", "away", dateTime));

        when(mockedCreateMatchUseCase.create("home", "away", dateTime, "leagueId"))
            .thenReturn(Match.builder().dateTime(dateTime).id(faker.name().toString()).build());

        scheduleLeagueMatchesUseCase.schedule(scheduled, "leagueId");
        verify(mockedCreateMatchUseCase, times(scheduled.size())).create(any(), any(), any(), any());
        verify(mockedMatchScheduler, times(scheduled.size())).scheduleMatch(any(), any());

    }
}