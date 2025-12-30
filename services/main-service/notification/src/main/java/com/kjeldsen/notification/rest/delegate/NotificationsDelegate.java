package com.kjeldsen.notification.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.rest.api.NotificationsApiDelegate;
import com.kjeldsen.notification.rest.mappers.NotificationMapper;
import com.kjeldsen.notification.rest.model.NotificationResponse;
import com.kjeldsen.notification.rest.model.SuccessResponse;
import com.kjeldsen.notification.usecases.GetNotificationUseCase;
import com.kjeldsen.notification.usecases.MarkAsReadNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationsDelegate implements NotificationsApiDelegate {

    private final GetNotificationUseCase getNotificationUseCase;
    private final MarkAsReadNotificationUseCase markAsReadNotificationUseCase;

    @Override
    @PreAuthorize("hasRole('ADMIN') or @accessAuthorizer.hasAccess(#teamId)")
    public ResponseEntity<List<NotificationResponse>> getAllNotificationByTeamId(String teamId) {
        List<Notification> notifications = getNotificationUseCase.get(teamId);
        return ResponseEntity.ok(NotificationMapper.INSTANCE.map(notifications));
    }

    @Override
    public ResponseEntity<SuccessResponse> markNotificationAsRead(String notificationId) {
        markAsReadNotificationUseCase.markAsReadNotification(notificationId, SecurityUtils.getCurrentTeamId());
        return ResponseEntity.ok(new SuccessResponse());
    }
}
