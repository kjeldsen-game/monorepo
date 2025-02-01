package com.kjeldsen.match.application.usecases;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetMatchAttendanceUseCaseTest {

    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase = new GetMatchAttendanceUseCase(teamReadRepository);

    @Test
    @DisplayName("Should throw error if home team not found")
    void should_throw_error_if_home_team_not_found() {
        when(teamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(Optional.empty());
        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            getMatchAttendanceUseCase.get("home", "away");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error if away team not found")
    void should_throw_error_if_away_team_not_found() {
        when(teamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(Optional.ofNullable(
            Team.builder()
                .fans(Team.Fans.builder().build())
                .buildings(Team.Buildings.builder().stadium(new Team.Buildings.Stadium()).build())
                .build()));
        when(teamReadRepository.findById(Team.TeamId.of("away"))).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            getMatchAttendanceUseCase.get("home", "away");
        }).getMessage());
    }

    @Test
    @DisplayName("Should return match attendance for match")
    void should_return_match_attendance_for_match() {
        when(teamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(Optional.ofNullable(
            Team.builder()
                .fans(Team.Fans.builder().build())
                .buildings(Team.Buildings.builder().stadium(new Team.Buildings.Stadium()).build())
                .build()));
        when(teamReadRepository.findById(Team.TeamId.of("away"))).thenReturn(Optional.ofNullable(
            Team.builder()
                .fans(Team.Fans.builder().build())
                .buildings(Team.Buildings.builder().stadium(new Team.Buildings.Stadium()).build())
                .build()));

        Map<String, Integer> result = getMatchAttendanceUseCase.get("home", "away");
        assertNotNull(result);
        assertInstanceOf(Integer.class, result.get("homeAttendance"));
        assertEquals(2, result.size());
        assertInstanceOf(Integer.class, result.get("awayAttendance"));
    }
}