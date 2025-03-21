package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.exceptions.SponsorDealAlreadySetException;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignSponsorIncomeUseCaseTest {

    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final SignSponsorIncomeUseCase signSponsorIncomeUseCase = new SignSponsorIncomeUseCase(
        mockedTeamWriteRepository, mockedCreateTransactionUseCase, mockedGetTeamUseCase);


    private static Team.TeamId testTeamId;

    @BeforeAll
    static void setUp() {
        testTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when sponsor is already signed")
    void should_throw_exception_when_sponsor_is_already_signed() {
        Team testTeam = TestData.generateTestTeam(testTeamId);
        testTeam.getEconomy().getSponsors().put(Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.MODERATE);
        when(mockedGetTeamUseCase.get(testTeamId)).thenReturn(testTeam);

        assertEquals("Sponsor deal is already set!", assertThrows(SponsorDealAlreadySetException.class, () -> {
            signSponsorIncomeUseCase.sign(testTeamId, Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.CONSERVATIVE);
        }).getMessage());

        verify(mockedGetTeamUseCase).get(testTeamId);
    }

    @Test
    @DisplayName("Should update sponsor and create transaction")
    void should_update_the_sponsor_and_create_transaction() {
        Team testTeam = TestData.generateTestTeam(testTeamId);
        testTeam.getEconomy().getSponsors().put(Team.Economy.IncomePeriodicity.ANNUAL, null);
        when(mockedGetTeamUseCase.get(testTeamId)).thenReturn(testTeam);

        signSponsorIncomeUseCase.sign(testTeamId, Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.MODERATE);

        assertEquals(Team.Economy.IncomeMode.MODERATE, testTeam.getEconomy().getSponsors().get(Team.Economy.IncomePeriodicity.ANNUAL));
        verify(mockedCreateTransactionUseCase).create(eq(testTeamId), eq(BigDecimal.valueOf(750000)),
            eq(Transaction.TransactionType.SPONSOR));
        verify(mockedGetTeamUseCase).get(testTeamId);
    }

    @Test
    @DisplayName("Should process the bonus income per match win from sponsor")
    void should_process_the_bonus_income_per_match_win_from_sponsor() {
        Team testTeam = TestData.generateTestTeam(testTeamId);
        testTeam.getEconomy().getSponsors().put(Team.Economy.IncomePeriodicity.ANNUAL, Team.Economy.IncomeMode.MODERATE);
        testTeam.getEconomy().getSponsors().put(Team.Economy.IncomePeriodicity.WEEKLY, null);

        when(mockedGetTeamUseCase.get(testTeamId)).thenReturn(Optional.of(testTeam));
        signSponsorIncomeUseCase.processBonus(testTeamId);

        verify(mockedCreateTransactionUseCase).create(eq(testTeamId), eq(BigDecimal.valueOf(200000)),
            eq(Transaction.TransactionType.SPONSOR));
        verifyNoMoreInteractions(mockedCreateTransactionUseCase);
        verify(mockedTeamReadRepository).findById(testTeamId);
    }
}