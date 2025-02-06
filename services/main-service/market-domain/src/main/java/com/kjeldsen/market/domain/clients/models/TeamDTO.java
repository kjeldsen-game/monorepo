package com.kjeldsen.market.domain.clients.models;

import com.kjeldsen.player.domain.Team;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class TeamDTO {

    private String id;
    private String name;
    private String leagueId;
    private List<PlayerDTO> players;
    private  Economy economy;


        @Getter
        @Setter
        @ToString
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Economy {
            @Field(targetType = FieldType.DECIMAL128)
            private BigDecimal balance;
            private Map<Team.Economy.PricingType, Integer> prices;
            private Map<Team.Economy.IncomePeriodicity, Team.Economy.IncomeMode> sponsors;
            private Team.Economy.BillboardDeal billboardDeal;
        }
}
