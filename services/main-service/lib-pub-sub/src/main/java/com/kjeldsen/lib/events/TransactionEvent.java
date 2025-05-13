package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class TransactionEvent extends Event {

    String teamId;
    BigDecimal transactionAmount;
    BigDecimal prevTransactionBalance;
    BigDecimal postTransactionBalance;
    String transactionType;
    String message;
}
