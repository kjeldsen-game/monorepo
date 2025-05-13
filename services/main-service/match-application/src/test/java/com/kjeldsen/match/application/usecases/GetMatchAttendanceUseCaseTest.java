package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.BuildingsClient;
import com.kjeldsen.lib.model.team.FansClient;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.Buildings;
import com.kjeldsen.match.domain.clients.models.team.Fans;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetMatchAttendanceUseCaseTest extends BaseClientTest {

    private final TeamClientApi teamClient = Mockito.mock(TeamClientApi.class);
    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase = new GetMatchAttendanceUseCase(teamClient);

    @Test
    @DisplayName("Should return match attendance for match")
    void should_return_match_attendance_for_match() {
        FansClient.FanTierClient fantier = FansClient.FanTierClient.builder().totalFans(10000).loyalty(50.0).build();
        when(teamClient.getTeam("home", null, null)).thenReturn(List.of(TeamClient.builder()
                .id("home")
                .fans(FansClient.builder().fanTiers(Map.of("1", fantier)).build())
                .buildings(BuildingsClient.builder().stadium(BuildingsClient.StadiumClient.builder().seats(1000).build()).build())
                .build())
            );

        when(teamClient.getTeam("away", null, null)).thenReturn(List.of(TeamClient.builder()
            .id("home")
            .fans(FansClient.builder().fanTiers(Map.of("1", fantier)).build())
            .buildings(BuildingsClient.builder().stadium(BuildingsClient.StadiumClient.builder().seats(1000).build()).build())
            .build())
        );


        Map<String, Integer> result = getMatchAttendanceUseCase.get("home", "away");
        assertNotNull(result);
        assertInstanceOf(Integer.class, result.get("homeAttendance"));
        assertEquals(2, result.size());
        assertInstanceOf(Integer.class, result.get("awayAttendance"));
    }
}