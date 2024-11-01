package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.BuildingMaintenanceExpenseUseCase;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuildingMaintenanceExpenseUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final BuildingMaintenanceExpenseUseCase buildingMaintenanceExpenseUseCase = new BuildingMaintenanceExpenseUseCase(
            mockedTeamReadRepository, mockedCreateTransactionUseCase);


    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeTestClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    public void should_throw_exception_when_team_is_null() {

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            buildingMaintenanceExpenseUseCase.expense(mockedTeamId);
        }).getMessage());
        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should pass variables to the CreateTransactionUseCase")
    public void should_pass_variables_to_the_CreateTransactionUseCase() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getBuildings().getStadium().setMaintenanceCost(BigDecimal.valueOf(100000));
        mockedTeam.getBuildings().setFacilities(new HashMap<>(Map.of(
            Team.Buildings.Facility.TRAINING_CENTER, new Team.Buildings.FacilityData(),
            Team.Buildings.Facility.YOUTH_PITCH, new Team.Buildings.FacilityData(),
            Team.Buildings.Facility.SPORTS_DOCTORS, new Team.Buildings.FacilityData(),
            Team.Buildings.Facility.VIDEO_ROOM, new Team.Buildings.FacilityData(),
            Team.Buildings.Facility.SCOUTS, new Team.Buildings.FacilityData()
        )));

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        buildingMaintenanceExpenseUseCase.expense(mockedTeamId);

        verify(mockedCreateTransactionUseCase, times(1)).create(
            eq(mockedTeamId),
            eq(BigDecimal.valueOf(-600_000)),
            eq(Transaction.TransactionType.BUILDING_MAINTENANCE)
        );
    }
}