package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterMarketPlayersQuery;
import com.kjeldsen.player.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
public class PlayerReadRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private PlayerMongoRepository playerMongoRepository;

    @Autowired
    private PlayerReadRepository playerReadRepository;

    @BeforeEach
    public void setup() {
        playerMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Get should")
    class GetShould {
        @Test
        @DisplayName("return Player based on the Query")
        void return_stored_tendency_for_provided_position_when_exist_in_database() {
            Player testPlayer = createTestPlayer();
            testPlayer.setId(Player.PlayerId.of("player1"));
            playerMongoRepository.save(testPlayer);
            List<Player> readPlayerList = playerReadRepository.filterMarketPlayers(FilterMarketPlayersQuery.builder()
                .playerIds(List.of(Player.PlayerId.of("player1"))).minAge(19).build());

            assertThat(readPlayerList).isNotEmpty();
            assertThat(readPlayerList).usingRecursiveComparison().isEqualTo(List.of(testPlayer));
        }

        @Test
        @DisplayName("Return players under certain age")
        public void should_return_players_under_certain_age() {
            List<Player> players = List.of(createTestPlayer(), createTestPlayer(), createTestPlayer());
            players.get(1).setAge(PlayerAge.builder().years(25).build());
            players.get(2).setAge(PlayerAge.builder().years(19).build());

            playerMongoRepository.saveAll(players);
            // Under age = lt not lte
            List<Player> readPlayers = playerReadRepository.findPlayerUnderAge(22);
            assertThat(readPlayers.size()).isEqualTo(1);
            assertThat(readPlayers).isNotEmpty();
        }


        @Test
        @DisplayName("Return players over certain age")
        public void should_return_players_over_certain_age() {
            List<Player> players = List.of(createTestPlayer(), createTestPlayer(), createTestPlayer());
            players.get(1).setAge(PlayerAge.builder().years(25).build());
            players.get(2).setAge(PlayerAge.builder().years(19).build());

            playerMongoRepository.saveAll(players);

            List<Player> readPlayers = playerReadRepository.findPlayerOverAge(22);
            assertThat(readPlayers.size()).isEqualTo(2);
            assertThat(readPlayers).isNotEmpty();
        }

        @Test
        @DisplayName("Return players based on skill filter")
        public void should_return_players_based_on_skill_filter() {
            Player testPlayer = createTestPlayer();
            testPlayer.setId(Player.PlayerId.of("player1"));
            playerMongoRepository.save(testPlayer);
            List<Player> readPlayerList = playerReadRepository.filterMarketPlayers(FilterMarketPlayersQuery.builder()
                .playerIds(List.of(Player.PlayerId.of("player1"))).minAge(19).skills(
                    List.of(FilterMarketPlayersQuery.PlayerSkillFilter.builder().minValue(12).playerSkill(PlayerSkill.SCORING).build())
                ).build());

            assertThat(readPlayerList).isNotEmpty();
            assertThat(readPlayerList).usingRecursiveComparison().isEqualTo(List.of(testPlayer));
        }

        @Test
        @DisplayName("Return mot return players because of skill filter")
        public void should_not_return_player_because_of_skill_filter() {
            Player testPlayer = createTestPlayer();
            testPlayer.setId(Player.PlayerId.of("player1"));
            playerMongoRepository.save(testPlayer);
            List<Player> readPlayerList = playerReadRepository.filterMarketPlayers(FilterMarketPlayersQuery.builder()
                .playerIds(List.of(Player.PlayerId.of("player1"))).minAge(19).skills(
                    List.of(FilterMarketPlayersQuery.PlayerSkillFilter.builder().minValue(55).playerSkill(PlayerSkill.SCORING).build())
                ).build());

            assertThat(readPlayerList).isEmpty();
        }
    }

    private Player createTestPlayer() {
        return playerMongoRepository.save(Player.builder().id(Player.PlayerId.generate()).position(
                PlayerPosition.FORWARD)
            .status(PlayerStatus.FOR_SALE)
            .age(PlayerAge.builder().years(22).months(2.0).days(2.0).build())
            .actualSkills(Map.of(
                PlayerSkill.SCORING, PlayerSkills.builder().potential(22).actual(12).build(),
                PlayerSkill.PASSING, PlayerSkills.builder().potential(55).actual(24).build(),
                PlayerSkill.REFLEXES, PlayerSkills.builder().potential(10).actual(2).build()
            ))
            .build());
    }
}
