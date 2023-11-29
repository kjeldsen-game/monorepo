package com.kjeldsen.match.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class PageController implements ErrorController {

    @RequestMapping("/")
    public RedirectView index(Authentication auth) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(auth == null ? "/signin.html" : "/home.html");
        return redirectView;
    }

    @RequestMapping("/error")
    public String error() {
        return "error.html";
    }
}
