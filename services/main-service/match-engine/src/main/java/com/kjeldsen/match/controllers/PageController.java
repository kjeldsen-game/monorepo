package com.kjeldsen.match.controllers;

import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserRepository userRepository;

    @RequestMapping("/match-engine")
    public RedirectView index() {
        Optional<User> user = userRepository.findAll().stream().findAny();
        RedirectView redirectView = new RedirectView();
        if (user.isPresent()) {
            redirectView.setUrl("/home.html");
        } else {
            redirectView.setUrl("/signup.html");
        }
        return redirectView;
    }

}
