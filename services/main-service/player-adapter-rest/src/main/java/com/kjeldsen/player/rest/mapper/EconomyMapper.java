package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.application.usecases.economy.GetPlayerWagesTransactionsUseCase;
import com.kjeldsen.player.application.usecases.economy.GetTeamTransactionsUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.model.BillboardDeal;
import com.kjeldsen.player.rest.model.PlayerWageTransaction;
import com.kjeldsen.player.rest.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = {IdMapper.class})
public interface EconomyMapper {

    EconomyMapper INSTANCE = Mappers.getMapper(EconomyMapper.class);

    @Mapping(target = "context", source = "entry.key")
    @Mapping(target = "lastWeekAmount", source = "entry.value.lastWeekSum")
    @Mapping(target = "thisSeasonAmount", source = "entry.value.seasonSum")
    @Mapping(target = "thisWeekAmount", source = "entry.value.weekSum")
    Transaction mapToTransaction(Map.Entry<String, GetTeamTransactionsUseCase.TeamTransactionSummary> entry);

    BillboardDeal mapToBillboardDeal(Team.Economy.BillboardDeal billboardDeal);

    PlayerWageTransaction mapToPlayerWageTransaction(GetPlayerWagesTransactionsUseCase.PlayerWageSummary playerWageSummary);

}
