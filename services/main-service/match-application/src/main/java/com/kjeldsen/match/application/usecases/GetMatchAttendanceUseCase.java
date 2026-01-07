package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchAttendanceUseCase {

    private final TeamClientApi teamClientApi;

    public Map<String, Integer> get(String homeId, String awayId) {
        log.info("GetMatchAttendanceUseCase for homeId={}, awayId={}", homeId, awayId);
        TeamResponse homeTeam = teamClientApi.getTeamById(homeId);
        TeamResponse awayTeam = teamClientApi.getTeamById(awayId);

//        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
//        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);
//        Integer awayAttendance = awayTeam.getFans().getTotalFans();
//
//        if (homeAttendance + awayAttendance > capacity) {
//            float scaleFactor = (float) (homeAttendance + awayAttendance) / capacity;
//            homeAttendance = Math.round(homeAttendance * scaleFactor);
//            awayAttendance = Math.round(awayAttendance * scaleFactor);
//        }
        return Map.of(
            "homeAttendance", 1000,
            "awayAttendance", 1000
        );
    }
}
