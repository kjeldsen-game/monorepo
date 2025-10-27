package com.kjeldsen.match.domain.schedulers;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface JobQueryService {
    Optional<ZonedDateTime> getNextFireTime(String jobName, String jobGroup);
}
