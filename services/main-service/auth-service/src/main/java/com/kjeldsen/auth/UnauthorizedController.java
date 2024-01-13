package com.kjeldsen.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnauthorizedController {

    @RequestMapping("/unauthorized")
    public ResponseEntity<String> unauthorized() {
        return ResponseEntity.badRequest().body("Unauthorized");
    }
}
