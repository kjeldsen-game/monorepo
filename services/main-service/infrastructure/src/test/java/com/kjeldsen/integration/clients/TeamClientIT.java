package com.kjeldsen.integration.clients;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TeamClientIT extends AbstractIT {

    @Autowired
    private TeamClientMatch teamClientMatch;
    @Autowired
    private TeamMongoRepository teamMongoRepository;

    @BeforeEach
    void setUp() {
        teamMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get a team")
    void should_make_a_request_to_the_backend() {
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("teamId")).build());

        TeamDTO result = teamClientMatch.getTeam("teamId", "mockToken");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("teamId");
    }
}
