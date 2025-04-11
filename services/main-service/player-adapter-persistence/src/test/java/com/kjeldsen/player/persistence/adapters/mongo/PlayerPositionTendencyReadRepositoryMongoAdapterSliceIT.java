package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkillRelevance;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
class PlayerPositionTendencyReadRepositoryMongoAdapterSliceIT extends AbstractMongoDbTest {

    @Autowired
    private PlayerPositionTendencyMongoRepository playerPositionTendencyMongoRepository;

    @Autowired
    private PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    @BeforeEach
    public void setup() {
        playerPositionTendencyMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Get should")
    class GetShould {

        @Test
        @DisplayName("return default tendency for provided position when there is not persisted one")
        void return_default_tendency_for_provided_position_when_no_exist_in_database() {
            PlayerPositionTendency actual = playerPositionTendencyReadRepository.get(PlayerPosition.FORWARD);
            assertThat(actual).isEqualTo(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES);
        }

        @Test
        @DisplayName("return default tendency for provided position when there is not persisted one")
        void return_stored_tendency_for_provided_position_when_exist_in_database() {
            PlayerPositionTendency storedPlayerPositionTendency = playerPositionTendencyMongoRepository.save(PlayerPositionTendency.builder()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE)))
                .build());

            PlayerPositionTendency actual = playerPositionTendencyReadRepository.get(PlayerPosition.FORWARD);

            assertThat(actual).usingRecursiveComparison().isEqualTo(PlayerPositionTendency.builder()
                .id(storedPlayerPositionTendency.getId())
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE)))
                .build());
        }
    }

    @Nested
    @DisplayName("Find should")
    class FindShould {
        @Test
        @DisplayName("return player position tendencies for all positions mainly the stored one or default one")
        void return_player_position_tendencies_for_all_positions_mainly_the_stored_one_or_default_one() {
            Integer noDefaultScoreSkill = 1;
            PlayerPositionTendency storedPlayerPositionTendency = PlayerPositionTendency.builder()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORING, new PlayerSkills(noDefaultScoreSkill, 0, PlayerSkillRelevance.CORE)))
                .build();
            playerPositionTendencyMongoRepository.save(storedPlayerPositionTendency);

            val actual = playerPositionTendencyReadRepository.find();

            assertThat(actual).hasSize(PlayerPositionTendency.DEFAULT_TENDENCIES.size());
            PlayerPositionTendency playerPositionTendency1 = actual.stream().filter(
                playerPositionTendency -> playerPositionTendency.getPosition().equals(PlayerPosition.FORWARD)).findFirst().orElseThrow();
            assertThat(playerPositionTendency1.getTendencies().get(PlayerSkill.SCORING)).usingRecursiveComparison().isEqualTo(
                new PlayerSkills(1, 0, PlayerSkillRelevance.CORE));
            assertThat(playerPositionTendency1.getTendencies())
                .doesNotContainEntry(PlayerSkill.SCORING, playerPositionTendency1.getTendencies().get(PlayerSkill.SCORING));
            PlayerPositionTendency.DEFAULT_TENDENCIES.stream()
                .filter(playerPositionTendency -> !playerPositionTendency.getPosition().equals(PlayerPosition.FORWARD))
                .forEach(playerPositionTendency -> assertThat(actual).contains(playerPositionTendency));

        }
    }

}
