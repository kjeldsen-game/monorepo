package com.kjeldsen.auth.domain.publishers;

import com.kjeldsen.player.domain.events.UserRegisterEvent;

public interface UserRegisterPublisher {
    void publishUserRegisterEvent(UserRegisterEvent userRegisterEvent);

}
