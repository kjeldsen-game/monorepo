package com.kjeldsen.integration.events;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.application.usecases.economy.BillboardIncomeUseCase;
import com.kjeldsen.player.application.usecases.economy.MatchAttendanceIncomeUseCase;
import com.kjeldsen.player.application.usecases.economy.MerchandiseIncomeUseCase;
import com.kjeldsen.player.application.usecases.economy.RestaurantIncomeUseCase;
import com.kjeldsen.player.application.usecases.economy.SignSponsorIncomeUseCase;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.MatchEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MatchEventListenerIT extends AbstractEventIT {

    @MockBean
    private MatchAttendanceIncomeUseCase matchAttendanceIncomeUsecase;
    @MockBean
    private FansManagementUsecase fansManagementUsecase;
    @MockBean
    private MerchandiseIncomeUseCase merchandiseIncomeUseCase;
    @MockBean
    private RestaurantIncomeUseCase restaurantIncomeUseCase;
    @MockBean
    private UpdateLoyaltyUseCase updateLoyaltyUseCase;
    @MockBean
    private SignSponsorIncomeUseCase signSponsorIncomeUseCase;

    @Test
    void should_handle_event_and_update_database() {

        MatchEvent matchEvent = MatchEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .matchId("matchId")
            .homeTeamId("homeTeamId")
            .awayTeamId("awayTeamId")
            .homeScore(1)
            .awayScore(2)
            .homeAttendance(100)
            .awayAttendance(100)
            .build();

        testEventPublisher.publishEvent(matchEvent);

        verify(matchAttendanceIncomeUsecase).income(any(), eq(200));
        verify(merchandiseIncomeUseCase).income(any(), eq(100));
        verify(restaurantIncomeUseCase).income(any(), eq(200));
//        verify(fansManagementUsecase).update(eq(Team.TeamId.of("homeTeamId")), any());
//        verify(fansManagementUsecase).update(eq(Team.TeamId.of("awayTeamId")), any());
//        verify(updateLoyaltyUseCase).updateLoyaltyMatch(eq(Team.TeamId.of("homeTeamId")), any(), any());
//        verify(updateLoyaltyUseCase).updateLoyaltyMatch(eq(Team.TeamId.of("awayTeamId")), any(), any());
        verify(signSponsorIncomeUseCase).processBonus(Team.TeamId.of("awayTeamId"));

    }
}
