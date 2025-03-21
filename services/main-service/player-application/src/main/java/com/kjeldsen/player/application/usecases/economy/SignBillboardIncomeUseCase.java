package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.exceptions.BillboardDealAlreadySetException;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RequiredArgsConstructor
@Component
@Slf4j
public class SignBillboardIncomeUseCase {

    private static final BigDecimal BASE_OFFER = BigDecimal.valueOf(100_000);
    private final TeamWriteRepository teamWriteRepository;
    private final BillboardIncomeUseCase billboardIncomeUseCase;
    private final GetTeamUseCase getTeamUseCase;

    public void sign(Team.TeamId teamId, Team.Economy.BillboardIncomeType type) {
        log.info("SignBillboardIncomeUseCase for team {} w type {}", teamId, type);

        Team team = getTeamUseCase.get(teamId);

        if (team.getEconomy().getBillboardDeal() != null) {
            throw new BillboardDealAlreadySetException();
        }

        Integer lastSeasonPosition = team.getLastSeasonPosition();
        Integer leagueCategory = 3;                  // TODO this have to be implemented JUST HARDCODED VALUE
        Integer averageAttendanceSeasonPosition = 5; // TODO this have to be implemented JUST HARDCODED VALUE

        BigDecimal finalOffer = getFinalOffer(type, lastSeasonPosition, leagueCategory, averageAttendanceSeasonPosition);

        team.getEconomy().setBillboardDeal(Team.Economy.BillboardDeal.builder()
            .startSeason(team.getActualSeason())
            .endSeason(team.getActualSeason() + type.getSeasonLength())
            .offer(finalOffer)
            .type(type).build());
        teamWriteRepository.save(team);
        billboardIncomeUseCase.pay(teamId);
        log.info("SignBillboardIncomeUseCase for team {} amount {} successfully completed!", teamId, finalOffer);
    }

    private BigDecimal getFinalOffer(Team.Economy.BillboardIncomeType type, Integer position,
        Integer leagueCategory, Integer averageAttendancePosition) {
        BigDecimal offer = BASE_OFFER.multiply(BigDecimal.valueOf(position)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
            .add(BASE_OFFER.multiply(BigDecimal.valueOf(averageAttendancePosition)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)))
            .add(BASE_OFFER.multiply(BigDecimal.valueOf(leagueCategory)
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP)));
        Random random = new Random();

        double adjustment = ((100 + (random.nextInt(51) - 25)) / 100.0);
        return offer.multiply(BigDecimal.valueOf(type.getSeasonLength())).multiply(BigDecimal.valueOf(adjustment));
    }
}
