package com.kjeldsen.player.application.usecases.fanbase;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FansEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FansManagementUsecaseTest {
    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final FansEventWriteRepository mockedFansEventWriteRepository = Mockito.mock(FansEventWriteRepository.class);
    private final FansManagementUsecase fansManagementUsecase = new FansManagementUsecase(
            mockedTeamReadRepository, mockedTeamWriteRepository, mockedFansEventWriteRepository);

    @Test
    @DisplayName("Should throw exception when team is null")
    public void should_throw_exception_when_team_is_null() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () ->
                fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_WIN)).getMessage());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

//
//    @Test
//    @DisplayName("Should update fans when based on league position")
//    public void should_update_fans_when_won_a_season() {
//        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
//        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
//        mockedTeam.getLeagueStats().setTablePosition(1);
//
//        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));
//
//        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.SEASON_POSITION);
//        assertEquals(1500, mockedTeam.getFans().getTotalFans());
//        verify(mockedTeamReadRepository).findById(mockedTeamId);
//        verifyNoMoreInteractions(mockedTeamReadRepository);
//    }


    @Test
    @DisplayName("Should update fans when impact is MATCH_WIN")
    public void should_update_fans_when_impact_is_win() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_WIN);
        assertEquals(10100, mockedTeam.getFans().getTotalFans());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should update fans when impact is MATCH_DRAW")
    public void should_update_fans_when_impact_is_draw() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_DRAW);
        assertEquals(10050, mockedTeam.getFans().getTotalFans());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should update fans when impact is MATCH_LOSS")
    public void should_update_fans_when_impact_is_loss() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_LOSS);
        assertEquals(9900, mockedTeam.getFans().getTotalFans());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }
}