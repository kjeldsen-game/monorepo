package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleTrainingUseCase {

    private static final int FIRST_DAY_OF_TRAINING = 0;
    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingBloomEventReadRepository playerTrainingBloomEventReadRepository;

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        validateDays(days);
        validateSkills(skills);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        IntStream.range(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(player, skill, currentDay)));
    }

    private void generateAndStoreEvent(Player player, PlayerSkill playerSkill, int currentDay) {

        Optional<PlayerBloomEvent> playerBloomEvent = playerTrainingBloomEventReadRepository.findOneByPlayerId(player.getId());

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .build();

        if (playerBloomEvent.isPresent() && player.isBloomActive(playerBloomEvent.get())) {
            playerTrainingEvent.setBloom(playerBloomEvent.get());
            playerTrainingEvent.setPoints(PointsGenerator.generatePointsRise(currentDay, playerBloomEvent.get().getBloomSpeedIncreaser()));
        } else {
            playerTrainingEvent.setPoints(PointsGenerator.generatePointsRise(currentDay));
        }

        playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }

    private void checkPointsMax(PlayerId playerId) {
        Player foundPlayer = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));
        int skillPoints = foundPlayer.getActualSkills().getSkillPoints(PlayerSkill.CO);

        // get al repository CQRS JI
        // leo desde mongo cuantos puntos de skill tiene el jugador y luego desde
        // el evento comprobar que al sumarse los puntos no llegue al tope y si
        // es así, que no este disponible a subir.
        //

    }

    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }

    public void validateSkills(List<PlayerSkill> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            throw new IllegalArgumentException("Skills cannot be null or empty");
        }
    }

}
