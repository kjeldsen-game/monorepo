package com.kjeldsen.integration.league;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.persistence.mongo.repositories.LeagueMongoRepository;
import com.kjeldsen.match.persistence.mongo.repositories.MatchMongoRepository;
import com.kjeldsen.match.rest.mapper.LeagueMapper;
import com.kjeldsen.match.rest.model.LeagueResponse;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LeagueApiIT extends AbstractIT {

    @Autowired
    private TeamMongoRepository teamMongoRepository;
    @Autowired
    private LeagueMongoRepository leagueMongoRepository;
    @Autowired
    private MatchMongoRepository matchMongoRepository;

    @BeforeEach
    void setup() {
        leagueMongoRepository.deleteAll();
        matchMongoRepository.deleteAll();
        teamMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("HTTP get to league by id")
    void should_return_200_when_getting_league_by_id() throws Exception {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            Map.of("exampleTeam", new League.LeagueStats(),
                "exampleTeam2", new League.LeagueStats())
        ).build();

        leagueMongoRepository.save(league);
        leagueMongoRepository.save(League.builder().id(League.LeagueId.of("leagueId2")).teams(
            Map.of("exampleTeam2", new League.LeagueStats())
        ).build());

        LeagueResponse response = LeagueMapper.INSTANCE.leagueResponseMap(league);

        MvcResult result = mockMvc.perform(get("/v1/league/leagueId"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        LeagueResponse actualResponse = objectMapper.readValue(jsonResponse, LeagueResponse.class);

        assertThat(actualResponse.getId()).isEqualTo(response.getId());
        assertThat(actualResponse.getTeams()).hasSameSizeAs(response.getTeams());
        assertThat(actualResponse.getTeams()).containsKeys("exampleTeam", "exampleTeam2");
    }

    @Test
    @DisplayName("Should trigger league schedule")
    void should_trigger_league_schedule() throws Exception {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            Map.of("exampleTeam", new League.LeagueStats(),
                "exampleTeam2", new League.LeagueStats(),
            "exampleTeam3", new League.LeagueStats(),
        "exampleTeam4", new League.LeagueStats())).build();

        leagueMongoRepository.save(league);
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("exampleTeam")).build());
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("exampleTeam2")).build());
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("exampleTeam3")).build());
        teamMongoRepository.save(Team.builder().id(Team.TeamId.of("exampleTeam4")).build());

        mockMvc.perform(put("/v1/league/leagueId"))
            .andExpect(status().isOk())
            .andReturn();

        List<Match> matches = matchMongoRepository.findAll();
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(12);
    }
}
