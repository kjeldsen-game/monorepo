package com.kjeldsen.auth.domain.repositories;

import com.kjeldsen.auth.domain.User;

public interface UserWriteRepository {

    User save(final User auction);

}
