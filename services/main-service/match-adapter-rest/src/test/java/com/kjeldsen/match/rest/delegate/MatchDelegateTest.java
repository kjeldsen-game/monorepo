package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.application.usecases.*;
import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.rest.model.EditPlayerRequest;
import com.kjeldsen.match.rest.model.PlayerPosition;
import com.kjeldsen.match.rest.model.PlayerStatus;
import com.kjeldsen.match.domain.validation.TeamFormationValidationResult;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkillRelevance;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Disabled
class MatchDelegateTest {

    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final MatchWriteRepository matchRepository = Mockito.mock(MatchWriteRepository.class);
    private final MatchReadRepository matchReadRepository = Mockito.mock(MatchReadRepository.class);

    private final GetMatchTeamUseCase getMatchTeamUseCase = Mockito.mock(GetMatchTeamUseCase.class);
    private final UpdateMatchLineupUseCase updateMatchLineupUseCase = Mockito.mock(UpdateMatchLineupUseCase.class);

    private final CreateMatchUseCase createMatchUseCase = Mockito.mock(CreateMatchUseCase.class);
    private final GetMatchUseCase getMatchUseCase = Mockito.mock(GetMatchUseCase.class);
    private final UpdateMatchChallengeUseCase updateMatchChallengeUseCase = Mockito.mock(UpdateMatchChallengeUseCase.class);
    private final ExecuteMatchUseCase executeMatchUseCase = Mockito.mock(ExecuteMatchUseCase.class);
    private final MatchDelegate matchDelegate = new MatchDelegate(playerReadRepository, matchRepository, getMatchTeamUseCase,
        updateMatchLineupUseCase,createMatchUseCase, getMatchUseCase, updateMatchChallengeUseCase, executeMatchUseCase);

    private Match match;

    private static final String TEST_PLAYER_ID = "1";

    @BeforeEach
    void setUp() {

        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        match = Match.builder()
            .id(java.util.UUID.randomUUID().toString())
            .home(home)
            .away(away)
            .build();
        when(matchReadRepository.findOneById(match.getId())).thenReturn(Optional.of(match));

        PlayerSkills skillPoints = new PlayerSkills(50, 0, PlayerSkillRelevance.RESIDUAL);
        com.kjeldsen.player.domain.Player player = com.kjeldsen.player.domain.Player.builder()
            .id(com.kjeldsen.player.domain.Player.PlayerId.of(TEST_PLAYER_ID))
            .status(com.kjeldsen.player.domain.PlayerStatus.INACTIVE)
            .position(com.kjeldsen.player.domain.PlayerPosition.FORWARD)
            .actualSkills(new HashMap<>(Map.of(PlayerSkill.SCORING, skillPoints)))
            .teamId(com.kjeldsen.player.domain.Team.TeamId.of(home.getId()))
            .build();
        when(playerReadRepository.findOneById(com.kjeldsen.player.domain.Player.PlayerId.of(TEST_PLAYER_ID))).thenReturn(Optional.of(player));
    }

    @Test
    void takePlayerOutOfBench() {
        Player p = match.getHome().getBench().stream().findFirst().get();
        EditPlayerRequest req = new EditPlayerRequest();
        req.setId(p.getId());
        req.setStatus(PlayerStatus.ACTIVE);
        matchDelegate.editPlayer(match.getId(), match.getHome().getId(), req);
        Assertions.assertEquals(com.kjeldsen.player.domain.PlayerStatus.ACTIVE, (p.getStatus()));
    }

    @Test
    void sendActivePlayerToBench() {
        Player p = match.getHome().getPlayers().stream().findFirst().get();
        EditPlayerRequest req = new EditPlayerRequest();
        req.setId(p.getId());
        req.setStatus(PlayerStatus.BENCH);
        matchDelegate.editPlayer(match.getId(), match.getHome().getId(), req);
        Assertions.assertEquals(com.kjeldsen.player.domain.PlayerStatus.BENCH, (p.getStatus()));
    }

    @Test
    void changePlayerPositionToCenterMidfielder() {
        Player player = match.getHome().getPlayers().stream()
            .filter(
                p -> !p.getPosition().equals(com.kjeldsen.player.domain.PlayerPosition.CENTRE_MIDFIELDER)) // Filtra los que no son CENTRE_MIDFIELDER
            .findFirst() // Encuentra el primer jugador que cumpla con la condición
            .orElse(null); // Maneja el caso donde no hay jugadores que cumplan la condición

        EditPlayerRequest req = new EditPlayerRequest();
        req.setId(player.getId());
        req.setPosition(PlayerPosition.CENTRE_MIDFIELDER);
        matchDelegate.editPlayer(match.getId(), match.getHome().getId(), req);
        Assertions.assertEquals(com.kjeldsen.player.domain.PlayerPosition.CENTRE_MIDFIELDER, player.getPosition());
    }

    @Test
    void addActivePlayer() {
        Player p = match.getHome().getPlayers().stream().findFirst().get();

        EditPlayerRequest req = new EditPlayerRequest();
        req.setId(TEST_PLAYER_ID);
        req.setStatus(PlayerStatus.ACTIVE);
        req.setPosition(PlayerPosition.CENTRE_MIDFIELDER);

        matchDelegate.addPlayer(match.getId(), match.getHome().getId(), req);

        Assertions.assertEquals(12, match.getHome().getPlayers().size());
    }

    @Test
    void removeActivePlayer() {
        Player p = match.getHome().getPlayers().stream().findFirst().get();
        matchDelegate.deletePlayer(match.getId(), match.getHome().getId(), p.getId());
        Assertions.assertEquals(10, match.getHome().getPlayers().size());
    }

    @Disabled
    @Test
    void checkValidationFailOnActivePlayerRemoval() throws IOException {
        Player p = match.getHome().getPlayers().stream().findFirst().get();

        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> preDeleteResponse = matchDelegate.validate(match.getId(), match.getHome().getId());
        TeamFormationValidationResult preDeleteResult = objectMapper.readValue(preDeleteResponse.getBody(), TeamFormationValidationResult.class);

        Assertions.assertTrue(preDeleteResult.getValid());

        matchDelegate.deletePlayer(match.getId(), match.getHome().getId(), p.getId());

        ResponseEntity<String> postDeleteResponse = matchDelegate.validate(match.getId(), match.getHome().getId());
        TeamFormationValidationResult result = objectMapper.readValue(postDeleteResponse.getBody(), TeamFormationValidationResult.class);

    }

}