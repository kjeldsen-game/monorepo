package com.kjeldsen.player.application.usecases.fanbase;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FansManagementUsecaseTest {
    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final FansManagementUsecase fansManagementUsecase = new FansManagementUsecase(
             mockedGetTeamUseCase, mockedTeamWriteRepository);

    @Test
    @DisplayName("Should update fans when impact is MATCH_WIN")
    public void should_update_fans_when_impact_is_win() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_WIN);
        assertEquals(10100, mockedTeam.getFans().getTotalFans());
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should update fans when impact is MATCH_DRAW")
    public void should_update_fans_when_impact_is_draw() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_DRAW);
        assertEquals(10050, mockedTeam.getFans().getTotalFans());
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should update fans when impact is MATCH_LOSS")
    public void should_update_fans_when_impact_is_loss() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        fansManagementUsecase.update(mockedTeamId, Team.Fans.ImpactType.MATCH_LOSS);
        assertEquals(9900, mockedTeam.getFans().getTotalFans());
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }
}