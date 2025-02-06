package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.Buildings;
import com.kjeldsen.match.domain.clients.models.team.Fans;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetMatchAttendanceUseCaseTest extends BaseClientTest {

    private final TeamClientMatch teamClient = Mockito.mock(TeamClientMatch.class);
    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase = new GetMatchAttendanceUseCase(teamClient);

    @Test
    @DisplayName("Should return match attendance for match")
    void should_return_match_attendance_for_match() {
        Fans.FanTier fantier = Fans.FanTier.builder().totalFans(10000).loyalty(50.0).build();
        when(teamClient.getTeam("home", "token")).thenReturn(
            TeamDTO.builder()
                .id("home")
                .fans(Fans.builder().fanTiers(Map.of("1", fantier)).build())
                .buildings(Buildings.builder().stadium(Buildings.Stadium.builder().seats(1000).build()).build())
                .build());

        when(teamClient.getTeam("away", "token")).thenReturn(
            TeamDTO.builder()
                .id("away")
                .fans(Fans.builder().fanTiers(Map.of("1", fantier)).build())
                .buildings(Buildings.builder().stadium(Buildings.Stadium.builder().seats(1000).build()).build())
                .build());

        Map<String, Integer> result = getMatchAttendanceUseCase.get("home", "away");
        assertNotNull(result);
        assertInstanceOf(Integer.class, result.get("homeAttendance"));
        assertEquals(2, result.size());
        assertInstanceOf(Integer.class, result.get("awayAttendance"));
    }
}