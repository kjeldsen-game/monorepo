package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        if (checkIfDaysIsValid(days) && checkNotNullOrEmpty(skills)) {
            IntStream.range(FIRST_DAY_OF_TRAINING, days)
                .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(playerId, skill, currentDay)));
        }
    }

    private void generateAndStoreEvent(PlayerId playerId, PlayerSkill playerSkill, int currentDay) {
        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(playerId)
            .skill(playerSkill)
            .points(PointsGenerator.generatePointsRise(currentDay))
            .build();
        playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }

    private void checkPointsMax(PlayerId playerId) {
        Player foundPlayer = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));
        int skillPoints = foundPlayer.getActualSkills().getSkillPoints(PlayerSkill.CO);

        // get al repository CQRS
        // leo desde mongo cuantos puntos de skill tiene el jugador y luego desde
        // el evento comprobar que al sumarse los puntos no llegue al tope y si
        // es así, que no este disponible a subir.
    }

    public boolean checkIfDaysIsValid(Integer days) {

        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        } else {
            return true;
        }
    }

    public boolean checkNotNullOrEmpty(List<PlayerSkill> skills) {

        if (skills == null || skills.isEmpty()) {
            throw new IllegalArgumentException("Skills cannot be null or empty");
        } else {
            return true;
        }
    }

}
