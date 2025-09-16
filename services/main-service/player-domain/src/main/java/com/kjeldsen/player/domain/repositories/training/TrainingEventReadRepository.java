package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TrainingEventReadRepository {

    List<TrainingEvent> findAllSuccessfulByTeamIdTypeOccurredAt(
        Team.TeamId teamId, TrainingEvent.TrainingType type, Instant since);

    Optional<TrainingEvent> findLatestByPlayerIdAndType(Player.PlayerId playerId, TrainingEvent.TrainingType type);

    Optional<TrainingEvent> findFirstByReferenceOrderByOccurredAtDesc(String reference);
}
