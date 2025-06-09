package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.duel.Duel;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.state.GameState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CarryoverTest {

    @ParameterizedTest
    @CsvSource({
        "60, 10, INITIATOR, CHALLENGER",
        "10, 60, CHALLENGER, INITIATOR"
    })
    @DisplayName("Should calculate the carryover value")
    void should_calculate_carryover_value(String initiatorTotal, String challengerTotal, String winner, String looser) {
        int initiatorTotalInt = Integer.parseInt(initiatorTotal);
        int challengerTotalInt = Integer.parseInt(challengerTotal);
        DuelRole winnerRole = DuelRole.valueOf(winner);
        DuelRole looserRole = DuelRole.valueOf(looser);

        DuelStats initiatorStats = Mockito.mock(DuelStats.class);
        DuelStats challengerStats = Mockito.mock(DuelStats.class);

        Play mockedLastPlay = Mockito.mock(Play.class);
        Duel mockedDuel = Mockito.mock(Duel.class);
        GameState mockedState = Mockito.mock(GameState.class);


        when(mockedState.lastPlay()).thenReturn(Optional.of(mockedLastPlay));
        when(mockedLastPlay.getDuel()).thenReturn(mockedDuel);
        when(mockedDuel.getInitiatorStats()).thenReturn(initiatorStats);
        when(mockedDuel.getChallengerStats()).thenReturn(challengerStats);

        when(initiatorStats.getTotal()).thenReturn(initiatorTotalInt);
        when(challengerStats.getTotal()).thenReturn(challengerTotalInt);

        Map<DuelRole, Integer> result = Carryover.getCarryover(mockedState);

        assertThat(result).containsKeys(DuelRole.INITIATOR, DuelRole.CHALLENGER);
        assertThat(result.get(winnerRole)).isEqualTo(25);
        assertThat(result.get(looserRole)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return 0 carryover when last play not present")
    void should_return_carryover_when_last_play_not_present() {
        GameState mockedState = Mockito.mock(GameState.class);
        when(mockedState.lastPlay()).thenReturn(Optional.empty());

        Map<DuelRole, Integer> result = Carryover.getCarryover(mockedState);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsKeys(DuelRole.CHALLENGER, DuelRole.INITIATOR);
        assertThat(result).containsValues(0,0);

    }
}