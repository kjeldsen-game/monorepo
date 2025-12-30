package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GetHistoryAuctionsUseCaseTest {

    private final AuctionReadRepository mockedAuctionReadRepository = org.mockito.Mockito.mock(AuctionReadRepository.class);
    private final GetHistoryAuctionsUseCase getHistoryAuctionsUseCase = new GetHistoryAuctionsUseCase(
        mockedAuctionReadRepository);

    @Test
    @DisplayName("Should throw error when teamId is null")
    void should_throw_error_when_teamId_is_null() {
        assertThrows(IllegalArgumentException.class, () -> getHistoryAuctionsUseCase.get(null, 1, 1));
    }

    @Test
    @DisplayName("Should return history auctions")
    void should_return_history_auctions() {
        Auction auction = Auction.builder()
            .id(Auction.AuctionId.generate())
            .teamId("team")
            .status(Auction.AuctionStatus.COMPLETED)
            .build();

        Page<Auction> pagedAuctions = new PageImpl<>(List.of(auction));
        Mockito.when(mockedAuctionReadRepository.findAllByQueryPaged(Mockito.any(FindAuctionsQuery.class)))
            .thenReturn(pagedAuctions);

        Page<Auction> result = getHistoryAuctionsUseCase.get("team", 1, 1);
        System.out.println(result);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}