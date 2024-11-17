package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.application.usecases.economy.GetPlayerWagesTransactionsUseCase;
import com.kjeldsen.player.application.usecases.economy.GetTeamTransactionsUseCase;
import com.kjeldsen.player.application.usecases.economy.GetTransactionsUseCaseAbstract;
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
    @Mapping(target = "thisSeasonAmount", source = "entry.value.seasonSummary")
    @Mapping(target = "thisWeekAmount", source = "entry.value.weekSummary")
    Transaction mapToTransaction(Map.Entry<String, GetTransactionsUseCaseAbstract.TransactionSummary> entry);

    BillboardDeal mapToBillboardDeal(Team.Economy.BillboardDeal billboardDeal);

    PlayerWageTransaction mapToPlayerWageTransaction(GetPlayerWagesTransactionsUseCase.PlayerWageSummary playerWageSummary);

}
