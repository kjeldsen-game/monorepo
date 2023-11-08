package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.domain.Event;
import com.kjeldsen.player.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Document(collection = "IncomeEvent")
@TypeAlias("IncomeEvent")
public class IncomeEvent extends Event {

    private Team.TeamId teamId;
    private Team.Economy.IncomeType incomeType;
    private Team.Economy.IncomePeriodicity incomePeriodicity;
    private Team.Economy.IncomeMode incomeMode;
    private BigDecimal amount;
    private Integer wins;

}
