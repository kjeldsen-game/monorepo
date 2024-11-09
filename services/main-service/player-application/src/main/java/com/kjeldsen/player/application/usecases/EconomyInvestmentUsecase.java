package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.EconomyInvestmentEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.EconomyInvestmentEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RequiredArgsConstructor
@Component
public class EconomyInvestmentUsecase {

    private static final BigDecimal MAX_KEY_VALUE_INVESTMENT_POINTS = BigDecimal.valueOf(5);
    private static final BigDecimal KEY_VALUE_INVESTMENT_AMOUNT = BigDecimal.valueOf(250_000);
    private static final BigDecimal MAX_INVESTMENT_AMOUNT = BigDecimal.valueOf(1_000_000);

    private final TeamReadRepository teamReadRepository;
    private final EconomyInvestmentEventWriteRepository economyInvestmentEventWriteRepository;
    private final CanteraInvestmentUsecase canteraInvestmentUsecase;

    public void invest(Team.TeamId teamId, BigDecimal amount) {
        log.info("Economy investment team {} with {} amount", teamId, amount);

        teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        EconomyInvestmentEvent economyInvestmentEvent = EconomyInvestmentEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(teamId)
            .amount(amount)
            .build();

        economyInvestmentEventWriteRepository.save(economyInvestmentEvent);

        canteraInvestmentUsecase.investToCanteraCategory(teamId, Team.Cantera.Investment.ECONOMY, amountToPoints(amount));
    }

    private Integer amountToPoints(BigDecimal amount) {
        if (amount.compareTo(KEY_VALUE_INVESTMENT_AMOUNT) > 0) {
            return (amount.multiply(MAX_KEY_VALUE_INVESTMENT_POINTS)).divide(MAX_INVESTMENT_AMOUNT, RoundingMode.HALF_DOWN).intValue();
        } else if (amount.compareTo(KEY_VALUE_INVESTMENT_AMOUNT) < 0) {
            return ((amount.multiply(MAX_KEY_VALUE_INVESTMENT_POINTS)).divide(KEY_VALUE_INVESTMENT_AMOUNT, RoundingMode.HALF_DOWN)).multiply(
                BigDecimal.ONE.negate()).intValue();
        } else {
            return 0;
        }
    }
}
