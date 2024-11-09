package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerTrainingEventReadRepositoryMongoAdapter implements PlayerTrainingEventReadRepository {

    private final PlayerTrainingEventMongoRepository playerTrainingEventMongoRepository;

    @Override
    public List<PlayerTrainingEvent> findAllByPlayerId(Player.PlayerId playerId) {
        return playerTrainingEventMongoRepository.findAllByPlayerId(playerId);
    }

    @Override
    public Optional<PlayerTrainingEvent> findLastByPlayerTrainingEvent(String playerTrainingEventId) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "occurredAt"));
        Page<PlayerTrainingEvent> page = playerTrainingEventMongoRepository
            .findLatestByPlayerTrainingScheduledEventId(playerTrainingEventId, pageRequest);

        return page.hasContent() ? Optional.of(page.getContent().get(0)) : Optional.empty();
    }
}
