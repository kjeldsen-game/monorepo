package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ResetBillboardIncomeUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final ResetBillboardIncomeUseCase resetBillboardIncomeUseCase = new ResetBillboardIncomeUseCase(
        mockedTeamReadRepository, mockedTeamWriteRepository);

    @Test
    @DisplayName("Should reset billboard deal")
    void shouldResetBillboardDeal() {
        Team.TeamId [] ids = new Team.TeamId[] {Team.TeamId.generate(), Team.TeamId.generate()};
        List<Team> teams = List.of(TestData.generateTestTeam(ids[0]),TestData.generateTestTeam(ids[1]));

        when(mockedTeamReadRepository.findAll()).thenReturn(teams);

        teams.get(0).getLeagueStats().clear();
        teams.get(1).getLeagueStats().clear();
        teams.get(0).getLeagueStats().put(2, Team.LeagueStats.builder().build());
        teams.get(1).getLeagueStats().put(2, Team.LeagueStats.builder().build());

        teams.get(0).getEconomy().setBillboardDeal(Team.Economy.BillboardDeal.builder().endSeason(2).build());
        teams.get(1).getEconomy().setBillboardDeal(Team.Economy.BillboardDeal.builder().endSeason(3).build());

        resetBillboardIncomeUseCase.reset();

        assertNull(teams.get(0).getEconomy().getBillboardDeal());
        assertNotNull(teams.get(1).getEconomy().getBillboardDeal());
    }
}