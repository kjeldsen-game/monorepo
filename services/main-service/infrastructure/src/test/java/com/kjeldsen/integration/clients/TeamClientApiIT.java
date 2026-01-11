package com.kjeldsen.integration.clients;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TeamClientApiIT extends AbstractIT {

    @Autowired
    private TeamClientApi teamClientApi;
    @Autowired
    private TeamMongoRepository teamMongoRepository;

    @BeforeEach
    void setUp() {
        teamMongoRepository.deleteAll();
        teamMongoRepository.save(Team.builder()
            .id(Team.TeamId.of("teamId"))
            .name("teamId").build());
    }

    @Test
    @DisplayName("Should get a team")
    void should_make_a_request_to_the_backend() {
        TeamResponse result = teamClientApi.getTeamById("teamId");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("teamId");
    }
}
