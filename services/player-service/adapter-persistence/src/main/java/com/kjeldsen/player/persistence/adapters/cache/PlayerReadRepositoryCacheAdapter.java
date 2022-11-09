package com.kjeldsen.player.persistence.adapters.cache;

import com.kjeldsen.player.application.repositories.PlayerReadRepository;
import com.kjeldsen.player.application.usecases.FindPlayersQuery;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.persistence.cache.PlayerInMemoryCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@ConditionalOnProperty(name = "service.persistence.adapter", havingValue = "cache", matchIfMissing = true)
@Component
@RequiredArgsConstructor
public class PlayerReadRepositoryCacheAdapter implements PlayerReadRepository {

    private final PlayerInMemoryCacheStore playerStore;


    @Override
    public Optional<Player> findOneById(PlayerId id) {
        return Optional.ofNullable(playerStore.get(id.value()));
    }

    @Override
    public List<Player> find(FindPlayersQuery query) {
        Pageable pageable = Pageable.ofSize(query.getSize()).withPage(query.getPage());
        Predicate<Player> filter =
            player -> query.getPosition() == null || player.getPosition() == query.getPosition();
        Comparator<Player> defaultSort = Comparator.comparing(player -> player.getId().value());

        return playerStore.find(filter, pageable, defaultSort).getContent();
    }
}
