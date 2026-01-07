package com.kjeldsen.market.application.mappers;

import com.kjeldsen.market.domain.AuctionPlayer;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AuctionPlayerMapper {

    AuctionPlayerMapper INSTANCE = Mappers.getMapper(AuctionPlayerMapper.class);

    AuctionPlayer map(PlayerResponse playerResponse);

}
