package com.kjeldsen.lib.model.team;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EconomyClient {
    private BigDecimal balance;
}
