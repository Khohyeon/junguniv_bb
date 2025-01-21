package com.example.junguniv_bb.domain._auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AuthController {
    @GetMapping("/login")
    public String loginForm(Model model) {

        return "common/auth/loginForm";
    }
}
