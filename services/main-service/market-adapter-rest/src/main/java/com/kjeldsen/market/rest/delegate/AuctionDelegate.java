package com.kjeldsen.market.rest.delegate;

import com.kjeldsen.market.application.GetHistoryAuctionsUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.rest.api.AuctionApiDelegate;
import com.kjeldsen.market.rest.mapper.AuctionMapper;
import com.kjeldsen.market.rest.model.PageableAuctionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionDelegate implements AuctionApiDelegate {

    private final GetHistoryAuctionsUseCase getHistoryAuctionsUseCase;

    @Override
    @PreAuthorize("hasRole('ADMIN') or @accessAuthorizer.hasAccess(#teamId)")
    public ResponseEntity<PageableAuctionResponse> getAuctionHistory(String teamId, Integer size, Integer page) {
        Page<Auction> result = getHistoryAuctionsUseCase.get(teamId, page, size);
        PageableAuctionResponse response = new PageableAuctionResponse()
            .totalElements((int) result.getTotalElements())
            .totalPages(result.getTotalPages())
            .page(result.getNumber())
            .content(result.getContent().stream()
                .map(AuctionMapper.INSTANCE::map)
                .toList());
        return ResponseEntity.ok(response);
    }
}
