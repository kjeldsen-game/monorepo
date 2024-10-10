package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamReadRepositoryMongoAdapter implements TeamReadRepository {

    private final TeamMongoRepository teamMongoRepository;

    @Override
    public Optional<Team> findByUserId(String id) {
        return teamMongoRepository.findOneByUserId(id);
    }

    @Override
    public Optional<Team> findById(Team.TeamId id) {
        return teamMongoRepository.findById(id);
    }

    @Override
    public Optional<Team> findOneById(TeamId id) {
        return teamMongoRepository.findById(id);
    }

    @Override
    public List<Team> find(FindTeamsQuery query) {
        Example<Team> teamDocumentExample = Example.of(Team.builder()
            .name(query.getName() != null ? query.getName() : null)
            .build());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        return teamMongoRepository.findAll(teamDocumentExample, pageable).stream().toList();
    }

    @Override
    public List<Team> findAll() {
        return teamMongoRepository.findAll();
    }
}
