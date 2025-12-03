package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.queries.FindTeamsQuery;
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
    public List<Team> find(FindTeamsQuery query) {
        Example<Team> teamDocumentExample = Example.of(Team.builder()
            .userId(query.getUserId() != null ? query.getUserId() : null)
            .name(query.getName() != null ? query.getName() : null)
            .build());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        return teamMongoRepository.findAll(teamDocumentExample, pageable).stream().toList();
    }

    @Override
    public List<Team> findAll() {
        return teamMongoRepository.findAll();
    }

    @Override
    public List<TeamId> findAllTeamIds() {
        return teamMongoRepository.findAllTeamIds();
    }

    @Override
    public Optional<Team> findByTeamName(String teamName) {
        return teamMongoRepository.findOneByTeamName(teamName);
    }

    @Override
    public List<Team> findAllByTeamIds(List<TeamId> teamIds) {
        return teamMongoRepository.findAllByTeamIds(teamIds);
    }
}
