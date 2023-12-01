package com.kjeldsen.match.engine.processing;

import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.mappers.PlaySnapshotMapper;
import com.kjeldsen.match.models.mappers.TeamSnapshotMapper;
import com.kjeldsen.match.models.snapshot.PlaySnapshot;
import com.kjeldsen.match.models.snapshot.TeamSnapshot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TeamSnapshotMapper teamSnapshotMapper;
    private final PlaySnapshotMapper playSnapshotMapper;

    public MatchReport generateReport(GameState endState, Team home, Team away) {

        TeamSnapshot homeSnapshot = teamSnapshotMapper.toSnapshot(home);
        TeamSnapshot awaySnapshot = teamSnapshotMapper.toSnapshot(away);

        List<PlaySnapshot> plays = endState.getPlays().stream()
            .map(playSnapshotMapper::toSnapshot)
            .toList();

        return new MatchReport(endState, plays, homeSnapshot, awaySnapshot);
    }
}
