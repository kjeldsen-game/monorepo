package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.generator.BloomPhaseGenerator;
import com.kjeldsen.player.domain.provider.PlayerCustomProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePlayersUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    public List<Player> generate(int numberOfPlayers, Team.TeamId teamId) {
        log.info("Generating {} players", numberOfPlayers);
        return IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> {
                PlayerPositionTendency positionTendency = playerPositionTendencyReadRepository.get(PlayerProvider.position());
                PlayerCategory playerCategory = i >= numberOfPlayers / 2 ? PlayerCategory.JUNIOR : PlayerCategory.SENIOR;
                return PlayerProvider.generate(teamId, positionTendency, playerCategory, 200);
            })
            .map(player -> {
                Player generatedPlayer = playerWriteRepository.save(player);
                log.info("Generated player {}", generatedPlayer);
                return generatedPlayer;
            }).toList();
    }

    public List<Player> generateCustomPlayers(Team.TeamId teamId) {
        List<Player> players = new ArrayList<>();
        List<PlayerCustomProvider.PlayerStats> stats = PlayerCustomProvider.getPlayerStatsList();
        for (int i = 0; i < stats.size(); i++) {
            PlayerCategory playerCategory = i >= stats.size() / 2 ? PlayerCategory.JUNIOR : PlayerCategory.SENIOR;

            Player player = Player.builder()
                .id(Player.PlayerId.generate())
                .name(PlayerProvider.name())
                .age(PlayerAge.generateAgeOfAPlayer(playerCategory))
                .position(stats.get(i).getPosition())
                .status(PlayerStatus.INACTIVE)
                .playerOrder(PlayerOrder.NONE)
                .bloomYear(BloomPhaseGenerator.generateBloomPhaseYear())
                .actualSkills(

                    stats.get(i).getPosition().equals(PlayerPosition.GOALKEEPER) ?
                        Map.of(
                            PlayerSkill.REFLEXES, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[0])  // Assuming stats[i][0] corresponds to REFLEXES
                                .potential(stats.get(i).getStats()[0])  // Assuming stats[i][0] corresponds to REFLEXES
                                .build(),
                            PlayerSkill.GOALKEEPER_POSITIONING, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[1])  // Assuming stats[i][1] corresponds to GOALKEEPER_POSITIONING
                                .potential(stats.get(i).getStats()[1])  // Assuming stats[i][1] corresponds to GOALKEEPER_POSITIONING
                                .build(),
                            PlayerSkill.INTERCEPTIONS, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[2])  // Assuming stats[i][2] corresponds to INTERCEPTIONS
                                .potential(stats.get(i).getStats()[2])  // Assuming stats[i][2] corresponds to INTERCEPTIONS
                                .build(),
                            PlayerSkill.CONTROL, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[3])  // Assuming stats[i][3] corresponds to CONTROL
                                .potential(stats.get(i).getStats()[3])  // Assuming stats[i][3] corresponds to CONTROL
                                .build(),
                            PlayerSkill.ORGANIZATION, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[4])  // Assuming stats[i][4] corresponds to ORGANIZATION
                                .potential(stats.get(i).getStats()[4])  // Assuming stats[i][4] corresponds to ORGANIZATION
                                .build(),
                            PlayerSkill.ONE_ON_ONE, PlayerSkills.builder()
                                .playerSkillRelevance(PlayerSkillRelevance.CORE)
                                .actual(stats.get(i).getStats()[5])  // Assuming stats[i][5] corresponds to ORGANIZATION
                                .potential(stats.get(i).getStats()[5])  // Assuming stats[i][5] corresponds to ORGANIZATION
                                .build()
                        )
                        :
                    Map.of(
                        PlayerSkill.SCORING, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[0])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[0])
                            .build(),
                        PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[1])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[1])
                            .build(),
                        PlayerSkill.BALL_CONTROL, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[2])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[2])
                            .build(),
                        PlayerSkill.PASSING, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[3])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[3])
                            .build(),
                        PlayerSkill.AERIAL, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[4])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[4])
                            .build(),
                        PlayerSkill.CONSTITUTION, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[5])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[5])
                            .build(),
                        PlayerSkill.TACKLING, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[6])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[6])
                            .build(),
                        PlayerSkill.DEFENSIVE_POSITIONING, PlayerSkills.builder()
                            .actual(stats.get(i).getStats()[7])
                            .playerSkillRelevance(PlayerSkillRelevance.CORE)
                            .potential(stats.get(i).getStats()[7])
                            .build()
                    )
                )
                .teamId(teamId)
                .category(playerCategory)
                .economy(Player.Economy.builder().build())
                .build();
            players.add(player);
            playerWriteRepository.save(player);
            player.negotiateSalary();
        }
        log.info("PlayerCustomProvider size={}", players.size());
        return players;
    }
}
