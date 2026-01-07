package com.kjeldsen.lib.clients;

import com.kjeldsen.auth.rest.model.ServiceTokenRequest;
import com.kjeldsen.auth.rest.model.TokenResponse;

public interface AuthClientApi {

    TokenResponse getServiceToken(ServiceTokenRequest request);
}
