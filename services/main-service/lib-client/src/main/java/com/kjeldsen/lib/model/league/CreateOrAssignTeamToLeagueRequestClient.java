package com.kjeldsen.lib.model.league;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrAssignTeamToLeagueRequestClient {

    String teamId;
    String teamName;
    BigDecimal teamValue;
}
