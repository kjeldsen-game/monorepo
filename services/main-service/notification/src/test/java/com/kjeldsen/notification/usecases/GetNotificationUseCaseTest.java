package com.kjeldsen.notification.usecases;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.repositories.NotificationReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


class GetNotificationUseCaseTest {

    private final NotificationReadRepository mockedNotificationReadRepository = Mockito.mock(NotificationReadRepository.class);
    private final GetNotificationUseCase getNotificationUseCase = new GetNotificationUseCase(mockedNotificationReadRepository);

    static Stream<Arguments> provideArrays() {
        return Stream.of(
            Arguments.of(List.of(), 0),
            Arguments.of(List.of(
                Notification.builder().teamId("teamId").build(),
                Notification.builder().teamId("teamId").build()), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArrays")
    @DisplayName("Should return list of the elements")
    void should_list_elements(List<Notification> notifications, int expectedSize) {
        when(mockedNotificationReadRepository.findByTeamIdAndIsReadOrderByCreatedAtDesc("teamId", false))
            .thenReturn(notifications);
        List<Notification> results = getNotificationUseCase.get("teamId");
        assertThat(results).hasSize(expectedSize);
    }
}