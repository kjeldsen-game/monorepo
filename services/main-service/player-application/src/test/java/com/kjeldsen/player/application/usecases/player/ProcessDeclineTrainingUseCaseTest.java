package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ProcessDeclineTrainingUseCaseTest {

    private static final Integer DECLINE_AGE_TRIGGER = 28;
    @Mock
    private PlayerReadRepository mockedPlayerReadRepository;
    @Mock
    private PlayerWriteRepository mockedPlayerWriteRepository;
    @Mock
    private PlayerTrainingDeclineEventReadRepository mockedPlayerTrainingDeclineEventReadRepository;
    @Mock
    private PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository;
    @InjectMocks
    private ProcessDeclineTrainingUseCase processDeclineTrainingUseCase;

    @Test
    @DisplayName("S")
    public void should_process_decline_training() {

    }
}