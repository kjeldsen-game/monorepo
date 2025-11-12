package com.kjeldsen.player.application.usecases.player.rating;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class ProcessRatingsUseCaseTest {

    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final ProcessRatingsUseCase processRatingsUseCase = new ProcessRatingsUseCase(
        mockedPlayerReadRepository, mockedPlayerWriteRepository);

    static Stream<Arguments> positionProvider() {
        return Stream.of(
            Arguments.of(PlayerPosition.FORWARD, 21.4),
            Arguments.of(PlayerPosition.CENTRE_BACK, 64.6),
            Arguments.of(PlayerPosition.DEFENSIVE_MIDFIELDER, 64.2),
            Arguments.of(PlayerPosition.OFFENSIVE_MIDFIELDER, 26.65),
            Arguments.of(PlayerPosition.LEFT_MIDFIELDER, 45.2 )
        );
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    @DisplayName("Should update rating on players")
    void should_update_rating_on_players_forwards(PlayerPosition position, Double actual) {
        List<Player> players = List.of(
            Player.builder()
                .preferredPosition(position)
                .actualSkills(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(10, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(20, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(30, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.PASSING, new PlayerSkills(40, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.AERIAL, new PlayerSkills(50, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(60, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.TACKLING, new PlayerSkills(70, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(80, 10, PlayerSkillRelevance.CORE)
                ))
                .build()
        );
        Mockito.when(mockedPlayerReadRepository.findAll()).thenReturn(players);

        processRatingsUseCase.process();
        assertThat(players.get(0).getRating().getActual()).isEqualTo(actual);
    }

    @Test
    @DisplayName("Should update ratings on goalkeeper")
    void should_update_ratings_on_goalkeeper() {
        List<Player> players = List.of(
            Player.builder()
                .preferredPosition(PlayerPosition.GOALKEEPER)
                .actualSkills(Map.of(
                    PlayerSkill.REFLEXES, new PlayerSkills(10, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.GOALKEEPER_POSITIONING, new PlayerSkills(20, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.INTERCEPTIONS, new PlayerSkills(30, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.CONTROL, new PlayerSkills(40, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.ORGANIZATION, new PlayerSkills(50, 10, PlayerSkillRelevance.CORE),
                    PlayerSkill.ONE_ON_ONE, new PlayerSkills(60, 10, PlayerSkillRelevance.CORE)
                ))
                .build()
        );
        Mockito.when(mockedPlayerReadRepository.findAll()).thenReturn(players);

        processRatingsUseCase.process();
        assertThat(players.get(0).getRating().getActual()).isEqualTo(27.0);
    }

}