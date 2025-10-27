package com.kjeldsen.match.persistence.adapters.mongo;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.persistence.common.AbstractMongoDbIT;
import com.kjeldsen.match.persistence.mongo.repositories.MatchMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchReadRepositoryMongoAdapterIT extends AbstractMongoDbIT {

    @Autowired
    private MatchMongoRepository matchMongoRepository;

    @Autowired
    private MatchReadRepository matchReadRepository;

    @BeforeEach
    public void setup() {
        matchMongoRepository.deleteAll();
    }

    @Test
    void should_find_matches_by_leagueId_status_and_teamId() {
        matchMongoRepository.saveAll(List.of(
            Match.builder().status(Match.Status.SCHEDULED).leagueId("123")
                .away(Team.builder().id("2").build())
                .home(Team.builder().id("1").build()
            ).build(),
            Match.builder().status(Match.Status.SCHEDULED).leagueId("123")
                .away(Team.builder().id("2").build())
                .home(Team.builder().id("1").build()
                ).build(),
            Match.builder().status(Match.Status.PLAYED).leagueId("32")
                .away(Team.builder().id("2").build())
                .home(Team.builder().id("4").build()
                ).build(),
            Match.builder().status(Match.Status.SCHEDULED).leagueId("123")
                .away(Team.builder().id("2").build())
                .home(Team.builder().id("7").build()
                ).build()
        ));

        assertThat(matchReadRepository.findMatchesByLeagueIdAndTeamIdAndStatus(
            "123", "7", Match.Status.SCHEDULED)).hasSize(1);
        assertThat(matchReadRepository.findMatchesByLeagueIdAndTeamIdAndStatus(
            "32", "4", Match.Status.PLAYED)).hasSize(1);
        assertThat(matchReadRepository.findMatchesByLeagueIdAndTeamIdAndStatus(
            "123", "2", Match.Status.SCHEDULED)).hasSize(3);
    }

}
