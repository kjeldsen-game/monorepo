//package com.kjeldsen.market.publisher;
//
//import com.kjeldsen.domain.EventId;
//import com.kjeldsen.market.domain.publishers.AuctionEndEventPublisher;
//import com.kjeldsen.player.domain.Team;
//import com.kjeldsen.player.domain.events.AuctionEndEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TestApplication.class)
//@Slf4j
//class AuctionEndEventPublisherIT {
//
//    @Autowired
//    private AuctionEndEventPublisher publisher;
//
//    @Autowired
//    private TestEventListener eventListener;
//
//    @BeforeEach
//    void setUp() {
//        eventListener.reset();
//    }
//
//    @Test
//    void test() {
//        publisher.publishAuctionEndEvent(AuctionEndEvent.builder()
//            .id(EventId.from("exampleEventId"))
//            .auctionWinner(Team.TeamId.of("winner"))
//            .auctionCreator(Team.TeamId.of("creator"))
//            .amount(BigDecimal.TEN)
//            .build());
//
//        AuctionEndEvent auctionEndEvent = eventListener.getEvents().get(0);
//        assertThat(eventListener.getEvents().size()).isEqualTo(1);
//        assertThat(auctionEndEvent.getAuctionCreator().value()).isEqualTo("creator");
//        assertThat(auctionEndEvent.getAuctionWinner().value()).isEqualTo("winner");
//        assertThat(auctionEndEvent.getAmount()).isEqualTo(BigDecimal.TEN);
//    }
//}