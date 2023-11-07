package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.player.PlayerReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerReadRepositoryMongoAdapter implements PlayerReadRepository {

    private final PlayerMongoRepository playerMongoRepository;

    @Override
    public Optional<Player> findOneById(Player.PlayerId id) {
        return playerMongoRepository.findById(id);
    }

    @Override
    public List<Player> find(FindPlayersQuery query) {
        Example<Player> playerDocumentExample = Example.of(Player.builder()
            .position(query.getPosition() != null ? query.getPosition() : null)
            .build());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        return playerMongoRepository.findAll(playerDocumentExample, pageable).stream().toList();
    }

    @Override
    public List<Player> findByTeamId(Team.TeamId teamId) {
        return playerMongoRepository.findByTeamId(teamId);
    }
}
