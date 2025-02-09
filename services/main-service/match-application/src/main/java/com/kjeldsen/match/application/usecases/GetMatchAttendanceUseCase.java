package com.kjeldsen.match.application.usecases;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchAttendanceUseCase {

    private final TeamClientMatch teamClient;

    public Map<String, Integer> get(String homeId, String awayId) {
        log.info("GetMatchAttendanceUseCase for homeId={}, awayId={}", homeId, awayId);
        TeamDTO homeTeam = teamClient.getTeam(homeId, SecurityUtils.getCurrentUserToken());
        TeamDTO awayTeam = teamClient.getTeam(awayId, SecurityUtils.getCurrentUserToken());

        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);
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
