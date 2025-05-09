package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.UpdateTeamPricingUsecase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateTeamPricingUsecaseTest {
    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final UpdateTeamPricingUsecase updateTeamPricingUsecase = new UpdateTeamPricingUsecase(
        mockedTeamWriteRepository, mockedGetTeamUseCase);

    @Test
    @DisplayName("Should throw exception when team price is higher than maximum")
    public void should_throw_exception_when_price_is_higher_than_maximum() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        // Max price for the merchandise is set to 25
        assertEquals("Price cannot be lower or higher than max or min price of item", assertThrows(IllegalArgumentException.class, () -> {
            updateTeamPricingUsecase.update(mockedTeamId, 26, Team.Economy.PricingType.MERCHANDISE);
        }).getMessage());
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should throw exception when team price is lower than maximum")
    public void should_throw_exception_when_price_is_lower_than_minimum() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        // Min price for the merchandise is set to 3
        assertEquals("Price cannot be lower or higher than max or min price of item", assertThrows(IllegalArgumentException.class, () -> {
            updateTeamPricingUsecase.update(mockedTeamId, 1, Team.Economy.PricingType.MERCHANDISE);
        }).getMessage());
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should update the price of the item")
    public void should_update_price_when_price_is_valid() {
        Team.TeamId mockedTeamId = TestData.generateTestTeamId();
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateTeamPricingUsecase.update(mockedTeamId, 12, Team.Economy.PricingType.MERCHANDISE);

        assertEquals(12, mockedTeam.getEconomy().getPrices().get(Team.Economy.PricingType.MERCHANDISE));
        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }
}