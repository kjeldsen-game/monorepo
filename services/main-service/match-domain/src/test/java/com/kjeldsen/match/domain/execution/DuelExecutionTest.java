package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.*;
import com.kjeldsen.match.domain.entities.duel.*;
import com.kjeldsen.match.domain.random.GausDuelRandomizer;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Disabled
class DuelExecutionTest {

    static Team initiatorTeam;
    static Team challengerTeam;
    static Player initiator;
    static Player challenger;
    static Player receiver;

    GameState mockedState = Mockito.mock(GameState.class);
    DuelParams mockedParams = Mockito.mock(DuelParams.class);
    BallState mockedBallState = Mockito.mock(BallState.class);

    @BeforeAll
    static void beforeAll() {
        initiator = Mockito.mock(Player.class);
        challenger = Mockito.mock(Player.class);
        initiatorTeam = RandomHelper.genTeam(TeamRole.HOME);
        challengerTeam = RandomHelper.genTeam(TeamRole.AWAY);
    }

    private static Stream<Arguments> handleBallControlDuelTestParameterProvider() {
        // Challenger, duelResult, challengerSkillValue
        return Stream.of(
            Arguments.of(null, DuelResult.WIN, 0),
            Arguments.of(challenger, DuelResult.LOSE, 50),
            Arguments.of(challenger, DuelResult.WIN, 10));
    }

    @ParameterizedTest
    @MethodSource("handleBallControlDuelTestParameterProvider")
    @DisplayName("Should execute handleBallControlDuel")
    void should_execute_handleBallControlDuel(Player challenger, DuelResult duelResult, int challengerSkillValue)  {

        DuelType duelType = DuelType.BALL_CONTROL;
        initMocks(duelType);

        try(MockedStatic<GausDuelRandomizer> gausMock = Mockito.mockStatic(GausDuelRandomizer.class);
            MockedStatic<Carryover> carryoverMock = Mockito.mockStatic(Carryover.class)) {

            carryoverMock.when(() -> Carryover.getCarryover(any())).thenReturn(
                Map.of(
                    DuelRole.CHALLENGER, 0,
                    DuelRole.INITIATOR, 0));

            gausMock.when(() -> GausDuelRandomizer.performance(any(), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().total(5.0).random(5.0).previousTotalImpact(0.0).build());

            when(initiator.duelSkill(any(), any(), any())).thenReturn(20);

            if (challenger != null) {
                when(challenger.duelSkill(any(), any(), any())).thenReturn(challengerSkillValue);
            }

            when(mockedParams.getDuelType()).thenReturn(duelType);

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);
            assertThat(result).isNotNull();
            assertThat(result.getResult()).isEqualTo(duelResult);
            assertThat(result.getDuelDisruption()).isNull();
        }
    }

    @ParameterizedTest
    @EnumSource(value = DuelType.class, names = {"PASSING_LOW", "PASSING_HIGH", "THROW_IN"})
    @DisplayName("Should throw error when receiver is null")
    void should_call_pass_duel_handler(DuelType duelType) {
        initMocks(duelType);
        assertThrows(GameStateException.class, () ->
            DuelExecution.executeDuel(mockedState, mockedParams));
    }

    @ParameterizedTest
    @MethodSource("handlePassDuelParametersProvider")
    @DisplayName("Should execute handlePassDuel with disruption")
    void ds(DuelType duelType, DuelResult duelResult, Optional<DuelDisruption> duelDisruptionOpt , int totalResult ) {
        initiator = Mockito.mock(Player.class);
        challenger = Mockito.mock(Player.class);
        receiver = Mockito.mock(Player.class);

        initMocks(duelType);
        when(mockedParams.getReceiver()).thenReturn(receiver);

        try (MockedStatic<DisruptionExecution> disruptionMock = Mockito.mockStatic(DisruptionExecution.class);
             MockedStatic<GausDuelRandomizer> gausMock = Mockito.mockStatic(GausDuelRandomizer.class);
             MockedStatic<Carryover> carryoverMock = Mockito.mockStatic(Carryover.class)) {

            disruptionMock.when(() -> DisruptionExecution.executeDisruption(any(), eq(DuelDisruptor.MISSED_PASS), any(), any()))
                .thenReturn(duelDisruptionOpt);

            carryoverMock.when(() -> Carryover.getCarryover(any())).thenReturn(
                Map.of(
                    DuelRole.CHALLENGER, 0,
                    DuelRole.INITIATOR, 0));

            gausMock.when(() -> GausDuelRandomizer.performance(any(), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().total(5.0).random(5.0).previousTotalImpact(0.0).build());

            when(initiator.duelSkill(any(), any(), any())).thenReturn(20);
            when(challenger.duelSkill(any(), any(), any())).thenReturn(10);

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);

            assertThat(result).isNotNull();
            if (duelDisruptionOpt.isPresent()) {
                assertThat(result.getDuelDisruption()).isNotNull();
            } else {
                assertThat(result.getDuelDisruption()).isNull();
            }
            assertThat(result.getInitiatorStats().getPerformance().getTotal()).isEqualTo(5.0);
            assertThat(result.getInitiatorStats().getTotal()).isEqualTo(25);
            assertThat(result.getResult()).isEqualTo(duelResult);
            assertThat(result.getChallengerStats().getTotal()).isEqualTo(totalResult);
        }
    }


    private void initMocks(DuelType duelType) {
        when(mockedState.getClock()).thenReturn(1);
        when(mockedState.getBallState()).thenReturn(mockedBallState);
        when(mockedParams.getDuelType()).thenReturn(duelType);
        when(mockedState.lastPlay()).thenReturn(Optional.empty());
        when(mockedParams.getState()).thenReturn(mockedState);
        when(mockedParams.getInitiator()).thenReturn(initiator);
        when(mockedParams.getChallenger()).thenReturn(challenger);
    }

    private static Stream<Arguments> handlePassDuelParametersProvider() {
        Optional<DuelDisruption> duelDisruptionOpt = Optional.of(DuelDisruption.builder()
            .destinationPitchArea(PitchArea.OUT_OF_BOUNDS)
            .build());

        return Stream.of(
            Arguments.of(DuelType.PASSING_HIGH, DuelResult.WIN, Optional.empty(), 15),
            Arguments.of(DuelType.PASSING_LOW, DuelResult.WIN, Optional.empty(), 15),
            Arguments.of(DuelType.THROW_IN, DuelResult.WIN, Optional.empty(), 0),

            Arguments.of(DuelType.PASSING_HIGH, DuelResult.LOSE, duelDisruptionOpt, 0),
            Arguments.of(DuelType.PASSING_LOW, DuelResult.LOSE, duelDisruptionOpt, 0),
            Arguments.of(DuelType.THROW_IN, DuelResult.LOSE, duelDisruptionOpt, 0));
    }


    @ParameterizedTest
    @EnumSource(value = DuelType.class, names = {"LOW_SHOT", "HEADER_SHOT", "ONE_TO_ONE_SHOT", "LONG_SHOT"})
    @DisplayName("Should throw error when challenger is not goalkeeper in shot duel")
    void should_throw_error_when_challenger_is_goalkeeper_in_shot_duel(DuelType duelType) {
        initMocks(duelType);
        when(challenger.getPosition()).thenReturn(PlayerPosition.FORWARD);
        assertThatThrownBy(() -> DuelExecution.executeDuel(mockedState, mockedParams))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Player defending shot is not a GOALKEEPER.");
    }

    @ParameterizedTest
    @EnumSource(value = DuelType.class, names = {"LOW_SHOT", "HEADER_SHOT", "ONE_TO_ONE_SHOT", "LONG_SHOT"})
    @DisplayName("Should execute handleShotDuel with disruption happened")
    void should_execute_handleShotDuel_with_disruption_happened(DuelType duelType) {

        initMocks(duelType);
        when(challenger.getPosition()).thenReturn(PlayerPosition.GOALKEEPER);

        try (MockedStatic<DisruptionExecution> disruptionMock = Mockito.mockStatic(DisruptionExecution.class)) {
            disruptionMock.when(() -> DisruptionExecution.executeDisruption(any(), any(), any(), any()))
                .thenReturn(Optional.of(DuelDisruption.builder().type(DuelDisruptor.MISSED_SHOT).build()));

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);
            assertThat(result).isNotNull();
            assertThat(result.getDuelDisruption()).isNotNull();
            assertThat(result.getChallengerStats()).isNull();
            assertThat(result.getResult()).isEqualTo(DuelResult.LOSE);
        }
    }

    private static Stream<Arguments> handleShotDuelTestParametersProvider() {
        return Stream.of(Arguments.of(DuelType.LOW_SHOT, DuelResult.WIN, false, 50),
            Arguments.of(DuelType.ONE_TO_ONE_SHOT, DuelResult.WIN, false, 50),
            Arguments.of(DuelType.HEADER_SHOT, DuelResult.WIN, false, 50),
            Arguments.of(DuelType.LONG_SHOT, DuelResult.WIN, false, 50),
            // Goalkeeper have higher skill and fumble did not happen
            Arguments.of(DuelType.LOW_SHOT, DuelResult.LOSE, false, 5),
            Arguments.of(DuelType.ONE_TO_ONE_SHOT, DuelResult.LOSE, false, 5),
            Arguments.of(DuelType.HEADER_SHOT, DuelResult.LOSE, false, 5),
            Arguments.of(DuelType.LONG_SHOT, DuelResult.LOSE, false, 5));
    }

    @ParameterizedTest
    @MethodSource("handleShotDuelTestParametersProvider")
    @DisplayName("Should execute handleShotDuel without disruption happened")
    void should_execute_handleShotDuel_without_disruption_happened(DuelType duelType,
       DuelResult duelResult, boolean fumbleDisruptionHappened, int initiatorSkillValue) {

        initMocks(duelType);
        when(challenger.getPosition()).thenReturn(PlayerPosition.GOALKEEPER);

        try (MockedStatic<DisruptionExecution> disruptionMock = Mockito.mockStatic(DisruptionExecution.class);
            MockedStatic<Carryover> carryoverMock = Mockito.mockStatic(Carryover.class);
            MockedStatic<GausDuelRandomizer> gausMock = Mockito.mockStatic(GausDuelRandomizer.class)) {
            disruptionMock.when(() -> DisruptionExecution.executeDisruption(any(), eq(DuelDisruptor.MISSED_SHOT), any(), any()))
                .thenReturn(Optional.empty());

            disruptionMock.when(() -> DisruptionExecution.executeDisruption(any(),
                eq(DuelDisruptor.GOALKEEPER_FUMBLE), any(), any())).thenReturn(
                    fumbleDisruptionHappened ? Optional.of(DuelDisruption.builder()
                    .type(DuelDisruptor.GOALKEEPER_FUMBLE).build()) : Optional.empty());

            when(initiator.duelSkill(any(), any(), any())).thenReturn(initiatorSkillValue);
            when(challenger.duelSkill(any(), any(), any())).thenReturn(10);

            carryoverMock.when(() -> Carryover.getCarryover(any())).thenReturn(
                Map.of(
                    DuelRole.CHALLENGER, 0,
                    DuelRole.INITIATOR, 0));

            gausMock.when(() -> GausDuelRandomizer.performance(any(), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().total(5.0).random(5.0).previousTotalImpact(0.0).build());

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);

            assertThat(result).isNotNull();
            assertThat(result.getResult()).isEqualTo(duelResult);
            if (initiatorSkillValue == 50) {
                assertThat(result.getInitiatorStats().getSkillPoints())
                    .isGreaterThan(result.getChallengerStats().getSkillPoints());
            }
        }
    }

    private static Stream<Arguments> handlePositionalDuelTestParametersProvider() {
        return Stream.of(Arguments.of(null, DuelResult.WIN, 0.0, 0),
            Arguments.of(challenger, DuelResult.WIN, 20.0, 20),
            Arguments.of(challenger, DuelResult.LOSE, 80.0, 80));
    }


    @ParameterizedTest
    @MethodSource("handlePositionalDuelTestParametersProvider")
    @DisplayName("Should execute handlePositionalDuel without counter attack and missed pass ")
    void should_execute_handlePositionalDuel_without_counterAttack_and_missedPass(Player challengerInput, DuelResult duelResult, double challengerRandom, int challengerTotal) {

        Map<ChainActionSequence, GameState.ChainAction> mockedChainActions = mock(Map.class);

        initMocks(DuelType.POSITIONAL);
        try(MockedStatic<AssistanceProvider> mockedAssist = Mockito.mockStatic(AssistanceProvider.class);
            MockedStatic<GausDuelRandomizer> mockedGaus = Mockito.mockStatic(GausDuelRandomizer.class)) {
            mockedAssist.when(() -> AssistanceProvider.getTeamAssistance(any(), any(), any())).thenReturn(
                Map.of("player1", 10, "player2", 10));

            mockedAssist.when(() -> AssistanceProvider.buildAssistanceByDuelRole(any(), any(), any()))
                .thenReturn(Map.of(DuelRole.INITIATOR, new DuelStats.Assistance(), DuelRole.CHALLENGER, new DuelStats.Assistance()));

            mockedGaus.when(() -> GausDuelRandomizer.performance(eq(initiator), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().previousTotalImpact(0.0).random(25.0).total(25.0).build());

            if (challengerInput != null) {
                mockedGaus.when(() -> GausDuelRandomizer.performance(eq(challengerInput), any(), any(), any())).thenReturn(
                    DuelStats.Performance.builder().previousTotalImpact(0.0).random(challengerRandom).total(challengerRandom).build());
            }

            when(mockedParams.getChallenger()).thenReturn(challengerInput);
            when(initiator.duelSkill(any(), any(), any())).thenReturn(10);
            when(mockedState.getChainActions()).thenReturn(mockedChainActions);
            when(mockedChainActions.getOrDefault(any(), any())).thenReturn(null);
            when(mockedState.lastPlay()).thenReturn(Optional.empty());

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);
            assertThat(result).isNotNull();
            assertThat(result.getResult()).isEqualTo(duelResult);
            assertThat(result.getInitiatorStats().getTotal()).isEqualTo(10 + 25);
            assertThat(result.getChallengerStats().getTotal()).isEqualTo(challengerTotal);

        }
    }

    private static Stream<Arguments> handlePositionalDuelTestParametersProviderMissedPass() {
        return Stream.of(
            Arguments.of( DuelResult.LOSE, 70.0),
            Arguments.of( DuelResult.WIN, 0.0));
    }


    @ParameterizedTest
    @MethodSource("handlePositionalDuelTestParametersProviderMissedPass")
    @DisplayName("Should execute handlePositionalDuel missed pass ")
    void should_execute_handlePositionalDuel_missedPass(DuelResult duelResult, double challengerRandom) {
        DuelDisruption duelDisruption = Mockito.mock(DuelDisruption.class);
        Map<ChainActionSequence, GameState.ChainAction> mockedChainActions = mock(Map.class);

        Play mockedPlay = mock(Play.class);
        Duel mockedDuel = mock(Duel.class);

        initMocks(DuelType.POSITIONAL);
        try(MockedStatic<AssistanceProvider> mockedAssist = Mockito.mockStatic(AssistanceProvider.class);
            MockedStatic<GausDuelRandomizer> mockedGaus = Mockito.mockStatic(GausDuelRandomizer.class)) {
            mockedAssist.when(() -> AssistanceProvider.getTeamAssistance(any(), any(), any())).thenReturn(
                Map.of("player1", 10, "player2", 10));

            mockedAssist.when(() -> AssistanceProvider.buildAssistanceByDuelRole(any(), any(), any()))
                .thenReturn(Map.of(DuelRole.INITIATOR, new DuelStats.Assistance(), DuelRole.CHALLENGER, new DuelStats.Assistance()));

            mockedGaus.when(() -> GausDuelRandomizer.performance(eq(initiator), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().previousTotalImpact(0.0).random(80.0).total(80.0).build());

            mockedGaus.when(() -> GausDuelRandomizer.performance(eq(challenger), any(), any(), any())).thenReturn(
                DuelStats.Performance.builder().previousTotalImpact(0.0).random(challengerRandom).total(challengerRandom).build());


            when(mockedParams.getChallenger()).thenReturn(challenger);
            when(mockedState.lastPlay()).thenReturn(Optional.of(mockedPlay));
            when(mockedPlay.getDuel()).thenReturn(mockedDuel);
            when(mockedDuel.getDuelDisruption()).thenReturn(duelDisruption);
            when(duelDisruption.getDestinationPitchArea()).thenReturn(PitchArea.CENTRE_MIDFIELD);
            when(initiator.duelSkill(any(), any(), any())).thenReturn(10);
            when(mockedState.getChainActions()).thenReturn(mockedChainActions);

            when(mockedChainActions.getOrDefault(any(), any())).thenReturn(new GameState.ChainAction());

            DuelDTO result = DuelExecution.executeDuel(mockedState, mockedParams);
            assertThat(result).isNotNull();
            assertThat(result.getResult()).isEqualTo(duelResult);
            assertThat(result.getInitiatorStats().getTotal()).isEqualTo(10 + 80);
            assertThat(result.getChallengerStats().getAssistance().getModifiersSum()).isEqualTo(50);
            assertThat(result.getChallengerStats().getAssistance().getModifiers()).containsKeys(ChainActionSequence.MISSED_PASS);
        }
    }

}