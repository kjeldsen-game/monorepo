package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.application.usecases.league.match.GenerateScheduledMatchesUseCase;
import com.kjeldsen.match.application.usecases.league.match.RescheduleLeagueUseCase;
import com.kjeldsen.match.application.usecases.league.match.ScheduleLeagueMatchesUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AddTeamToActiveLeagueUseCaseTest {

    private final  RescheduleLeagueUseCase mockedRescheduleLeagueUseCase = Mockito.mock(RescheduleLeagueUseCase.class);

    private final GenerateScheduledMatchesUseCase mockedGenerateScheduledMatchesUseCase = Mockito.mock(GenerateScheduledMatchesUseCase.class);

    private final ScheduleLeagueMatchesUseCase mockedScheduleLeagueMatchesUseCase = Mockito.mock(ScheduleLeagueMatchesUseCase.class);
    @Mock
    private LeagueWriteRepository mockedLeagueWriteRepository;
    @Mock
    private LeagueReadRepository mockedLeagueReadRepository;
    @InjectMocks
    private AddTeamToActiveLeagueUseCase addTeamToActiveLeagueUseCase = new AddTeamToActiveLeagueUseCase(
        mockedRescheduleLeagueUseCase, mockedGenerateScheduledMatchesUseCase, mockedScheduleLeagueMatchesUseCase);


    @Test
    @DisplayName("Should add team to the league without spot")
    void should_add_team_to_the_league_without_spot() {
        addTeamToActiveLeagueUseCase.addWithoutSpot("teamName", "teamId");
        Mockito.verify(mockedLeagueWriteRepository, Mockito.times(1))
            .save(Mockito.any(League.class));
        Mockito.verify(mockedScheduleLeagueMatchesUseCase, Mockito.times(1)).schedule(
            Mockito.anyList(), Mockito.anyString());
    }
}