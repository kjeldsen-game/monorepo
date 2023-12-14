package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

class UpdatePlayerPositionTendencyUseCaseTest {

    private final PlayerPositionTendencyWriteRepository mockedPlayerPositionTendencyWriteRepository = Mockito.mock(
        PlayerPositionTendencyWriteRepository.class);
    private final UpdatePlayerPositionTendencyUseCase updatePlayerPositionTendencyUseCase = new UpdatePlayerPositionTendencyUseCase(
        mockedPlayerPositionTendencyWriteRepository);

    @Test
    @DisplayName("update a player position tendency by a given position and tendencies")
    public void update_a_player_position_tendency_by_a_given_position_and_tendencies() {
        PlayerSkills v1 = new PlayerSkills(5, 0, PlayerSkillRelevance.CORE);
        PlayerSkills v2 = new PlayerSkills(4, 0, PlayerSkillRelevance.CORE);
        updatePlayerPositionTendencyUseCase.update(
            new UpdatePlayerTendencies(PlayerPosition.FORWARD, Map.of(PlayerSkill.SCORING, v1, PlayerSkill.OFFENSIVE_POSITIONING, v2)));

        PlayerPositionTendency expectedPlayerPositionToBeSaved = PlayerPositionTendency.builder().position(PlayerPosition.FORWARD).tendencies(
            Map.of(PlayerSkill.SCORING, v1, PlayerSkill.OFFENSIVE_POSITIONING, v2)).build();
        Mockito.verify(mockedPlayerPositionTendencyWriteRepository)
            .save(Mockito.eq(expectedPlayerPositionToBeSaved));
    }
}
