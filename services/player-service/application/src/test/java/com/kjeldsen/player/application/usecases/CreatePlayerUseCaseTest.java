package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerTendency;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePlayerUseCaseTest {

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private CreatePlayerUseCase createPlayerUseCase = new CreatePlayerUseCase(mockedPlayerWriteRepository);

    @Nested
    @DisplayName("Handle should")
    class Handle {

        @Test
        void should_save_generated_player() {
            NewPlayer command = NewPlayer.builder()
                    .age(PlayerAge.of(20))
                    .position(PlayerPosition.MIDDLE)
                    .playerTendency(PlayerTendency.AGGRESSIVE)
                    .points(100)
                    .build();

            createPlayerUseCase.create(command);

            ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
            Mockito.verify(mockedPlayerWriteRepository, Mockito.times(1)).save(argumentCaptor.capture());

            Player playerToSave = argumentCaptor.getValue();
            assertThat(playerToSave)
                    .matches(player -> player.getAge().equals(PlayerAge.of(20))
                            && player.getPosition().equals(PlayerPosition.MIDDLE)
                            && StringUtils.isNotBlank(player.getName().value()));
        }
    }
}