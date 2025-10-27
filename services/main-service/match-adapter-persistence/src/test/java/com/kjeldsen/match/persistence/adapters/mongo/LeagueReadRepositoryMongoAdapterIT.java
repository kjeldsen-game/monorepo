package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.persistence.common.AbstractMongoDbIT;
import com.kjeldsen.match.persistence.mongo.repositories.LeagueMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LeagueReadRepositoryMongoAdapterIT extends AbstractMongoDbIT {

    @Autowired
    private LeagueMongoRepository leagueMongoRepository;

    @Autowired
    private LeagueReadRepository leagueReadRepository;

    @BeforeEach
    public void setup() {
        leagueMongoRepository.deleteAll();
    }

    @Test
    void should_get_all_by_status() {
        leagueMongoRepository.saveAll(List.of(
            League.builder().id(League.LeagueId.generate()).status(League.LeagueStatus.PRESEASON).build(),
            League.builder().id(League.LeagueId.generate()).status(League.LeagueStatus.PRESEASON).build(),
            League.builder().id(League.LeagueId.generate()).status(League.LeagueStatus.ACTIVE).build(),
            League.builder().id(League.LeagueId.generate()).status(League.LeagueStatus.PRESEASON).build()
            ));

        List<League> leagues = leagueReadRepository.findAllByStatus(League.LeagueStatus.ACTIVE);
        assertNotNull(leagues);
    }

}
