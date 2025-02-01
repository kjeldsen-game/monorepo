package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchAttendanceUseCase {

    private final TeamReadRepository teamReadRepository;

    public Map<String, Integer> get(String homeId, String awayId) {
        com.kjeldsen.player.domain.Team homeTeam = teamReadRepository.findById(Team.TeamId.of(homeId))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);

        com.kjeldsen.player.domain.Team awayTeam = teamReadRepository.findById(Team.TeamId.of(awayId))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer awayAttendance = awayTeam.getFans().getTotalFans();

        if (homeAttendance + awayAttendance > capacity) {
            float scaleFactor = (float) (homeAttendance + awayAttendance) / capacity;
            homeAttendance = Math.round(homeAttendance * scaleFactor);
            awayAttendance = Math.round(awayAttendance * scaleFactor);
        }
        return Map.of(
            "homeAttendance", homeAttendance,
            "awayAttendance", awayAttendance
        );
    }
}
