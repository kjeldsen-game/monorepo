package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingDeclineEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerTrainingDeclineEventReadRepositoryMongoAdapter implements PlayerTrainingDeclineEventReadRepository {

    private final PlayerTrainingDeclineEventMongoRepository playerTrainingDeclineEventMongoRepository;

    @Override
    public Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(Player.PlayerId id) {
        return playerTrainingDeclineEventMongoRepository.findOneByPlayerId(id);
    }

    @Override
    public Optional<PlayerTrainingDeclineEvent> findLatestByPlayerId(Player.PlayerId id) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "occurredAt"));

        Page<PlayerTrainingDeclineEvent> page = playerTrainingDeclineEventMongoRepository.findLatestByPlayerId(id, pageRequest);
        return page.hasContent() ? Optional.of(page.getContent().get(0)) : Optional.empty();

    }
}
