package com.yoda_of_soda.springfolio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {
    @GetMapping("/login")
    public String LoginPage() {
        return "index";
    }

    @GetMapping("/signup")
    public String SignUpPage() {
        return "signup";
    }
}
