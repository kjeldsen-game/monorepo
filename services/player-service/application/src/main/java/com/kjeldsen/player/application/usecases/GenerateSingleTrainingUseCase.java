package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.persistence.events.EventId;
import com.kjeldsen.player.persistence.events.PlayerTrainedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleTrainingUseCase {

    private static final int FIRST_DAY_OF_TRAINING = 0;

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        IntStream.range(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(playerId, skill, currentDay)));
    }

    private void generateAndStoreEvent(PlayerId playerId, PlayerSkill playerSkill, int currentDay) {
        PlayerTrainedEvent.builder()
            .eventId(EventId.generate())
            .eventDate(Instant.now())
            .playerId(playerId)
            .skill(playerSkill)
            .points(generatePoints(currentDay))
            .build();

        // TODO y el evento PlayerTrainedEvent se lo mandas a un repository de tipo write (mira ejemplos hechos) y lo guardas.
        // luego te metes a la base de datos y compruebas que este ahi
        // cuando este ahi, buscas CQRS y lees un poco sobre el tema y te ves un video. PAra que entiendas por que has usado un reposiotrio al que
        // llamas write (veras que hay read tmb) :D
    }

    private int generatePoints(int currentDay) {
        // TODO dali mandale ahi
        if (currentDay < 2) {
            return 2;
        }
        return 5;
    }

}
