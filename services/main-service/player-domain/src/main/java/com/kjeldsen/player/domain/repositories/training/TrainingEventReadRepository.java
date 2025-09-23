package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TrainingEventReadRepository {

    List<TrainingEvent> findAllSuccessfulByTeamIdTypePlayerPositionOccurredAt(
        Team.TeamId teamId, TrainingEvent.TrainingType type, Instant since, PlayerPosition position);

    Optional<TrainingEvent> findLatestByPlayerIdAndType(Player.PlayerId playerId, TrainingEvent.TrainingType type);

    Optional<TrainingEvent> findFirstByReferenceOrderByOccurredAtDesc(String reference);
}
