package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyWriteRepository;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.documents.PlayerPositionTendencyDocument;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPositionTendencyMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class), excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test")
class PlayerPositionTendencyWriteRepositoryMongoAdapterSliceTest extends AbstractMongoDbTest {

    @Autowired
    private PlayerPositionTendencyMongoRepository playerPositionTendencyMongoRepository;

    @Autowired
    private PlayerPositionTendencyWriteRepository playerPositionTendencyWriteRepository;

    @BeforeEach
    public void setup() {
        playerPositionTendencyMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Save should")
    class SaveShould {

        @Test
        @DisplayName("create a new player position tendency when there is not existed one for provided position")
        public void create_a_new_player_position_tendency_when_there_is_not_existed_one_for_provided_position() {
            PlayerPositionTendency forwardTendencies = PlayerPositionTendency.builder()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, 5))
                .build();

            playerPositionTendencyWriteRepository.save(forwardTendencies);

            Optional<PlayerPositionTendencyDocument> actual = playerPositionTendencyMongoRepository.findByPosition(PlayerPosition.FORWARD);

            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getTendencies().get(PlayerSkill.SCORE)).isEqualTo(5);
        }

        @Test
        @DisplayName("update an existed player position tendency when there is existed one for provided position")
        public void update_an_existed_player_position_tendency_when_there_is_existed_one_for_provided_position() {
            PlayerPositionTendencyDocument forwardTendencyDocument = PlayerPositionTendencyDocument.builder()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, 5))
                .build();
            playerPositionTendencyMongoRepository.save(forwardTendencyDocument);

            PlayerPositionTendency forwardTendencies = PlayerPositionTendency.builder()
                .position(PlayerPosition.FORWARD)
                .tendencies(Map.of(PlayerSkill.SCORE, 10))
                .build();

            playerPositionTendencyWriteRepository.save(forwardTendencies);

            Optional<PlayerPositionTendencyDocument> actual = playerPositionTendencyMongoRepository.findByPosition(PlayerPosition.FORWARD);

            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getTendencies().get(PlayerSkill.SCORE)).isEqualTo(10);
        }
    }

}
