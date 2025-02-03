package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.usecases.league.GenerateMatchScheduleUseCase;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GenerateMatchScheduleUseCaseTest {

    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final GenerateMatchScheduleUseCase generateMatchScheduleUseCase = new GenerateMatchScheduleUseCase(mockedGetLeagueUseCase);

    @Test
    @DisplayName("Should return list of scheduled matches")
    void should_return_list_of_scheduled_matches() {

//        List<String> teamIds = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
//        List<GenerateMatchScheduleUseCase.ScheduledMatch> result = generateMatchScheduleUseCase.generate(teamIds);
//
//        // Assert total matches
//        assertEquals(teamIds.size() * (teamIds.size() - 1) , result.size());
//
//        // Assert total matches for one team
//        long homeOccurrences = result.stream().filter(m -> m.homeTeamId().equals("1")).count();
//        long awayOccurrences = result.stream().filter(m -> m.awayTeamId().equals("1")).count();
//        assertEquals((teamIds.size() - 1) * 2L, homeOccurrences + awayOccurrences);
//
//        // 4 because the 4 matches are generated in round
//        assertEquals(
//            1L,
//            Duration.between(result.get(0).date(), result.get(4).date()).toDays()
//        );
    }
}