package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamId;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import com.kjeldsen.player.persistence.mongo.documents.TeamDocument;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamWriteRepositoryMongoAdapter implements TeamWriteRepository {

    private final TeamMongoRepository teamMongoRepository;

    @Override
    public TeamId save(Team team, String userId) {
        TeamDocument teamToStore = TeamDocument.from(team, userId);
        TeamDocument storedTeam = teamMongoRepository.save(teamToStore);
        return TeamId.of(storedTeam.getId());
    }
}
