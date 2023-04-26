package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.event.DuelStartedEvent;
import com.kjeldsen.match.domain.id.EventId;
import com.kjeldsen.match.domain.provider.InstantProvider;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PitchArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DuelTest {
    @Test
    void should_create_won_duel() {
        int ALWAYS_WINNING = 100;
        int ALWAYS_LOOSING = 0;

        Player attacker = Player.builder().actualSkills(Map.of(Player.PlayerSkill.PASSING, ALWAYS_WINNING)).build();
        Player defender = Player.builder().actualSkills(Map.of(Player.PlayerSkill.TACKLING, ALWAYS_LOOSING)).build();
        DuelStartedEvent duelStartedEvent = DuelStartedEvent.builder()
                .eventId(EventId.generate())
                .eventDate(InstantProvider.now())
                .attacker(attacker)
                .defender(defender)
                .duelType(DuelType.PASS)
                .modifiers(List.of())
                .pitchArea(PitchArea.ONE)
                .build();

        Duel duel = Duel.generate(duelStartedEvent);

        Assertions.assertTrue(duel.isWin());
    }

    @Test
    void should_create_lost_duel() {
        int ALWAYS_WINNING = 100;
        int ALWAYS_LOOSING = 0;

        Player attacker = Player.builder().actualSkills(Map.of(Player.PlayerSkill.PASSING, ALWAYS_LOOSING)).build();
        Player defender = Player.builder().actualSkills(Map.of(Player.PlayerSkill.TACKLING, ALWAYS_WINNING)).build();
        DuelStartedEvent duelStartedEvent = DuelStartedEvent.builder()
                .eventId(EventId.generate())
                .eventDate(InstantProvider.now())
                .attacker(attacker)
                .defender(defender)
                .duelType(DuelType.PASS)
                .modifiers(List.of())
                .pitchArea(PitchArea.ONE)
                .build();

        Duel duel = Duel.generate(duelStartedEvent);

        Assertions.assertTrue(duel.isLose());
    }
}