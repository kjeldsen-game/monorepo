package com.kjeldsen.notification.repositories;

import com.kjeldsen.notification.models.Notification;

import java.util.List;

public interface NotificationWriteRepository {

    Notification save(Notification notification);

    void saveAll(List<Notification> notifications);
}
