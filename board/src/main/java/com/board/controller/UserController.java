package com.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("signUp")
    public String signUpPage(Model model) {
        return "user/signUp";
    }

    @GetMapping("signIn")
    public String signInPage(Model model) {
        return "user/signIn";
    }
}
