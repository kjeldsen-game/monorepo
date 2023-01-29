package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
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
    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        IntStream.range(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(playerId, skill, currentDay)));
    }

    private void generateAndStoreEvent(PlayerId playerId, PlayerSkill playerSkill, int currentDay) {
        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(Instant.now())
            .playerId(playerId)
            .skill(playerSkill)
            .points(generateRise(currentDay))
            .build();
        playerTrainingEventWriteRepository.save(playerTrainingEvent);

        // TODO y el evento PlayerTrainedEvent se lo mandas a un repository de tipo write (mira ejemplos hechos) y lo guardas.
        // luego te metes a la base de datos y compruebas que este ahi
        // cuando este ahi, buscas CQRS y lees un poco sobre el tema y te ves un video. PAra que entiendas por que has usado un reposiotrio al que
        // llamas write (veras que hay read tmb) :D

    }

    private int generateRise(int currentDay) {

        double randomProbabilityRise = Math.random();
        float probability = 1 / 14;
        double probabilityRaise = probability * currentDay; // Sumatorio de la prob diaria por el numero de dias que lleva
        int risePoints = 0;

        if (randomProbabilityRise <= probabilityRaise) {
            risePoints = generatePoints();
        }

        return risePoints;
    }

    private int generatePoints() {

        double randomProbabilityPoints = Math.random() * 100;// Generacion de numero random entre 0-100

        if (randomProbabilityPoints <= 35) {
            return 1;
        } else if (randomProbabilityPoints > 35 && randomProbabilityPoints <= 70) {
            return 2;
        } else if (randomProbabilityPoints > 70 && randomProbabilityPoints <= 85) {
            return 3;
        } else if (randomProbabilityPoints > 85 && randomProbabilityPoints <= 95) {
            return 4;
        } else {
            return 5;
        }
    }


    // TODO dali mandale ahi
//        if (currentDay < 2) {
//            return 2;
//        }
//        return 5;
//    }

}
