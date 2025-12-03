package com.kjeldsen.lib.events.market;

import com.kjeldsen.lib.events.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@ToString
public class BidEvent extends Event {
    String teamId;
    BigDecimal amount;
}
