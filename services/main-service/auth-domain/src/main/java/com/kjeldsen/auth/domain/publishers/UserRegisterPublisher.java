package com.kjeldsen.auth.domain.publishers;

import com.kjeldsen.lib.events.UserRegisterEvent;

public interface UserRegisterPublisher {
    void publishUserRegisterEvent(UserRegisterEvent userRegisterEvent);

}
