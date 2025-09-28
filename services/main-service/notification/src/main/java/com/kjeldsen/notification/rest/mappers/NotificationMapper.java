package com.kjeldsen.notification.rest.mappers;

import com.kjeldsen.notification.models.Notification;
import com.kjeldsen.notification.rest.model.NotificationResponse;
import com.kjeldsen.notification.rest.model.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationResponse map(Notification notification);

    NotificationType map(Notification.NotificationType notificationType);

    List<NotificationResponse> map(List<Notification> notifications);
}
