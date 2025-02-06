package com.kjeldsen.integration.clients;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamClientIT extends AbstractIT {

    @Autowired
    private TeamClientMatch teamClientMatch;
    @Autowired
    private TeamMongoRepository teamMongoRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        teamMongoRepository.deleteAll();
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + 15001)
            .build();
    }

    @Test
    @Disabled
    @DisplayName("Should make a request to the backend")
    void should_make_a_request_to_the_backend() {
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("teamId")).build());

        TeamDTO result = teamClientMatch.getTeam("teamId", "mockToken");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("teamId");
    }
}
