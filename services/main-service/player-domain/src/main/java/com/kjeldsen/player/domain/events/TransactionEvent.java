package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Document(collection = "TransactionEvents")
@TypeAlias("TransactionEvent")
public class TransactionEvent extends Event {

    Team.TeamId teamId;
    BigDecimal transactionAmount;
    BigDecimal prevTransactionBalance;
    BigDecimal postTransactionBalance;
    Transaction.TransactionType transactionType;
    String message;
}
