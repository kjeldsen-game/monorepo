package com.kjeldsen.market.domain.schedulers;

import java.time.Instant;

public interface AuctionEndJobScheduler {
     void scheduleAuctionEndJob (String auctionId, Instant endTime);
     void rescheduleAuctionEndJob (String auctionId, Instant endTime);
}
