//package com.kjeldsen.player.rest.mapper;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.kjeldsen.player.domain.Player;
//import com.kjeldsen.player.domain.Team;
//import com.kjeldsen.player.domain.Transaction;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//class IdMapperTest {
//
//    static Stream<Object[]> idProvider() {
//        return Stream.of(
//            new Object[]{new Player.PlayerId("player-123"), "player-123"},
//            new Object[]{new Team.TeamId("team-456"), "team-456"},
//            new Object[]{new Transaction.TransactionId("txn-789"), "txn-789"},
//            new Object[]{null, null}
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("idProvider")
//    void testMapIds(Object id, String expected) {
//        String result = IdMapper.getValue(id);
//        assertEquals(expected, result);
//    }
//}
