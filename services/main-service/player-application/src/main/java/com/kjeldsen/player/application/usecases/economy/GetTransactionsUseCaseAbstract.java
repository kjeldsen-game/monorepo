package com.kjeldsen.player.application.usecases.economy;


import com.kjeldsen.player.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public abstract class GetTransactionsUseCaseAbstract {

    protected BigDecimal typeAmountPerPeriods(List<Transaction> transactions, Predicate<Transaction> filterCondition ) {
        return transactions.stream()
            .filter(filterCondition)
            .map(Transaction::getTransactionAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TransactionSummary {
        private BigDecimal weekSummary = BigDecimal.ZERO;
        private BigDecimal seasonSummary = BigDecimal.ZERO;
    }
}
