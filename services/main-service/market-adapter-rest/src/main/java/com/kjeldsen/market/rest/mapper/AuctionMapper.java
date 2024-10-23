package com.kjeldsen.market.rest.mapper;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.rest.model.AuctionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface AuctionMapper {

    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

    AuctionResponse auctionResponseMap(Auction auction);
}
