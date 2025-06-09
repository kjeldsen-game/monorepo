package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.entities.duel.DuelDisruption;
import com.kjeldsen.match.domain.entities.duel.DuelDisruptor;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.generator.RandomGenerator;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class DisruptionExecutionTest {

    @Test
    @DisplayName("Should return optional empty when no last play is present")
    void should_return_optional_empty() {
        GameState mockedGameState = Mockito.mock(GameState.class);
        when(mockedGameState.lastPlay()).thenReturn(Optional.empty());
        Integer total = 50;
        DuelParams duelParams = DuelParams.builder().build();

        Optional<DuelDisruption> result = DisruptionExecution.executeDisruption(duelParams, DuelDisruptor.MISSED_PASS, mockedGameState, total);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return optional if action type is throw in")
    void should_return_optional_if_action_type_is_throw_in() {
        GameState mockedGameState = Mockito.mock(GameState.class);
        when(mockedGameState.lastPlay()).thenReturn(
            Optional.ofNullable(Play.builder().build()));
        Integer total = 50;

        DuelParams duelParams = DuelParams.builder().duelType(DuelType.THROW_IN).build();
        Optional<DuelDisruption> result = DisruptionExecution.executeDisruption(duelParams, DuelDisruptor.MISSED_PASS, mockedGameState, total);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should not be executed when total is over 100")
    void should_not_be_executed_if_total_is_over_100() {
        GameState mockedGameState = Mockito.mock(GameState.class);
        when(mockedGameState.lastPlay()).thenReturn(
            Optional.ofNullable(Play.builder().build()));
        Integer total = 101;

        DuelParams duelParams = DuelParams.builder().duelType(DuelType.PASSING_HIGH).build();
        Optional<DuelDisruption> result = DisruptionExecution.executeDisruption(duelParams, DuelDisruptor.MISSED_PASS, mockedGameState, total);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should execute the duel disruption calculation without new Receiver")
    void should_execute_the_duel_disruption_calculation() {
        GameState mockedGameState = Mockito.mock(GameState.class);
        when(mockedGameState.lastPlay()).thenReturn(
            Optional.ofNullable(Play.builder().build()));
        Integer total = 50;

        DuelParams mockedParams = Mockito.mock(DuelParams.class);
        PitchArea mockedPitchArea = Mockito.mock(PitchArea.class);
        List<PitchArea> mockedNearbyAreas = Mockito.mock(List.class);

        when(mockedParams.getDuelType()).thenReturn(DuelType.PASSING_HIGH);
        when(mockedParams.getDestinationPitchArea()).thenReturn(mockedPitchArea);
        when(mockedPitchArea.getNearbyAreas()).thenReturn(mockedNearbyAreas);
        when(mockedNearbyAreas.get(anyInt())).thenReturn(PitchArea.OUT_OF_BOUNDS);

        try(MockedStatic<RandomGenerator> mockedStatic = Mockito.mockStatic(RandomGenerator.class)) {

            mockedStatic.when(() -> RandomGenerator.randomInt(0, 100)).thenReturn(11);

            Optional<DuelDisruption> result = DisruptionExecution
                .executeDisruption(mockedParams, DuelDisruptor.MISSED_PASS, mockedGameState, total);

            assertThat(result).isNotEmpty();
            assertThat(result.get().getDifference()).isEqualTo((double) (100 - total) / 2);
            assertThat(result.get().getReceiver()).isEqualTo(null);
            assertThat(result.get().getType()).isEqualTo(DuelDisruptor.MISSED_PASS);
        }
    }


    @Test
    @DisplayName("Should execute the duel disruption calculation with new receiver")
    void should_execute_the_duel_disruption_calculation_with_new_receiver() {
        GameState mockedGameState = Mockito.mock(GameState.class);
        DuelParams mockedParams = Mockito.mock(DuelParams.class);
        PitchArea mockedPitchArea = Mockito.mock(PitchArea.class);
        List<PitchArea> mockedNearbyAreas = Mockito.mock(List.class);

        Integer total = 50;

        when(mockedParams.getDuelType()).thenReturn(DuelType.PASSING_HIGH);
        when(mockedParams.getDestinationPitchArea()).thenReturn(mockedPitchArea);
        when(mockedPitchArea.getNearbyAreas()).thenReturn(mockedNearbyAreas);
        when(mockedNearbyAreas.get(anyInt())).thenReturn(PitchArea.CENTRE_MIDFIELD);
        when(mockedGameState.lastPlay()).thenReturn(
            Optional.ofNullable(Play.builder().build()));

        Team team = RandomHelper.genTeam(TeamRole.HOME);
        when(mockedParams.getState()).thenReturn(mockedGameState);
        when(mockedGameState.attackingTeam()).thenReturn(TeamState.init(team));

        try(MockedStatic<RandomGenerator> mockedStatic = Mockito.mockStatic(RandomGenerator.class)) {

            mockedStatic.when(() -> RandomGenerator.randomInt(0, 100)).thenReturn(11);

            Optional<DuelDisruption> result = DisruptionExecution
                .executeDisruption(mockedParams, DuelDisruptor.MISSED_PASS, mockedGameState, total);

            assertThat(result).isNotEmpty();
            assertThat(result.get().getDifference()).isEqualTo((double) (100 - total) / 2);
            assertThat(result.get().getReceiver()).isNotNull();
            assertThat(result.get().getType()).isEqualTo(DuelDisruptor.MISSED_PASS);
            assertThat(result.get().getDestinationPitchArea()).isEqualTo(PitchArea.CENTRE_MIDFIELD);
            assertThat(result.get().getReceiver().getPosition().isCentral()).isTrue();
        }
    }

    @ParameterizedTest
    @CsvSource({
        "50, 99, false",
        "50, 10, true"
    })
    @DisplayName("Should execute handleMissedShot disruption without success")
    void should_execute_handleMissedShot_disruption_without_success(String totalString, String randomString, String booleanString) {
        int totalSkill = Integer.parseInt(totalString);
        int random = Integer.parseInt(randomString);
        boolean booleanValue = Boolean.parseBoolean(booleanString);

        DuelParams mockedParams = Mockito.mock(DuelParams.class);
        GameState mockedGameState = Mockito.mock(GameState.class);

        DuelDisruptor duelDisruptor = DuelDisruptor.MISSED_SHOT;
        try (MockedStatic<RandomGenerator> mockedStatic = Mockito.mockStatic(RandomGenerator.class)) {
            mockedStatic.when(() -> RandomGenerator.randomInt(0, 100)).thenReturn(random);
            Optional<DuelDisruption> result = DisruptionExecution.executeDisruption(
                mockedParams, duelDisruptor, mockedGameState, totalSkill);


            if (booleanValue) {
                assertThat(result).isNotEmpty();
                assertThat(result.get().getType()).isEqualTo(DuelDisruptor.MISSED_SHOT);
                assertThat(result.get().getDifference()).isEqualTo( 12.5);
            } else {
                assertThat(result).isEmpty();
            }
        }
    }
}