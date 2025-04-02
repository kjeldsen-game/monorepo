package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.application.usecases.facilities.UpgradeBuildingUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpgradeBuildingUseCaseTest {

    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final UpgradeBuildingUseCase upgradeBuildingUseCase = new UpgradeBuildingUseCase(mockedGetTeamUseCase,
            mockedTeamWriteRepository, mockedCreateTransactionUseCase);

    private static Team.TeamId mockedTeamId;
    @BeforeAll
    static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when there are not free slots")
    public void should_throw_exception_when_there_are_not_free_slots() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getBuildings().setFreeSlots(1);
        Team.Buildings.Facility mockedFacility = Team.Buildings.Facility.SCOUTS;
        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            upgradeBuildingUseCase.upgrade(mockedTeamId, mockedFacility);
        });

        assertEquals("Cannot increase " + mockedFacility + " facility level, because there are no free slots"
                , thrown.getMessage());

        verify(mockedGetTeamUseCase, times(1)).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should throw exception when facility is on max level")
    public void should_throw_exception_when_facility_is_one_max_level() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        Team.Buildings.Facility mockedFacility = Team.Buildings.Facility.SCOUTS;
        mockedTeam.getBuildings().getFacilities().get(mockedFacility).setLevel(10);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            upgradeBuildingUseCase.upgrade(mockedTeamId, mockedFacility);
        });
        assertEquals("Cannot increase " + mockedFacility + " facility level, because it is on max level"
                , thrown.getMessage());

        verify(mockedGetTeamUseCase, times(1)).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should pass right values for transaction creation for STADIUM")
    public void should_pass_right_values_for_transaction_creation_for_STADIUM() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        Team.Buildings.Facility mockedFacility = Team.Buildings.Facility.STADIUM;
        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        BigDecimal expectedTransactionAmount = BigDecimal.valueOf(100_000);
        upgradeBuildingUseCase.upgrade(mockedTeamId, mockedFacility);

        assertEquals(2, mockedTeam.getBuildings().getStadium().getLevel());
        assertEquals(BigDecimal.valueOf(mockedTeam.getBuildings().getStadium().getSeats()),
                mockedTeam.getBuildings().getStadium().getMaintenanceCost());

        verify(mockedCreateTransactionUseCase, times(1)).create(
                eq(mockedTeamId),
                eq(expectedTransactionAmount),
                eq(Transaction.TransactionType.BUILDING_UPGRADE)
        );

    }

    @Test
    @DisplayName("Should pass right values for transaction creation for SCOUTS")
    public void should_pass_right_values_for_transaction_creation_for_SCOUTS() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getBuildings().setFreeSlots(25);
        Team.Buildings.Facility mockedFacility = Team.Buildings.Facility.SCOUTS;
        BigDecimal expectedTransactionAmount = BigDecimal.valueOf(200_000);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        upgradeBuildingUseCase.upgrade(mockedTeamId, mockedFacility);

        assertEquals(2, mockedTeam.getBuildings().getFacilities().get(mockedFacility).getLevel());
        assertEquals(BigDecimal.valueOf(200_000), mockedTeam.getBuildings().getFacilities()
                .get(mockedFacility).getMaintenanceCost());

        verify(mockedCreateTransactionUseCase, times(1)).create(
                eq(mockedTeamId),
                eq(expectedTransactionAmount),
                eq(Transaction.TransactionType.BUILDING_UPGRADE)
        );
    }
}