package com.kjeldsen.player.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExampleQuartzTestUseCase {

    public void execute() {
        log.info("Executing QuartzTestUseCase");
    }
}
