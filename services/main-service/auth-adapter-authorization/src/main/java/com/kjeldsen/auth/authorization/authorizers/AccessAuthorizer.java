package com.kjeldsen.auth.authorization.authorizers;

import com.kjeldsen.auth.authorization.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("accessAuthorizer")
@Slf4j
public class AccessAuthorizer {

    public boolean hasAccess(String requestedTeamId) {
//        log.info("Checking if team access is authorized for requested team {}", requestedTeamId);
        String currentTeamId = SecurityUtils.getCurrentTeamId();
        return currentTeamId != null && currentTeamId.equals(requestedTeamId);
    }
}
