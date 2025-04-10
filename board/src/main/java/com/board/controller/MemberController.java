package com.board.controller;

import com.board.dto.MemberDto;
import com.board.entity.Member;
import com.board.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("signUp")
    public String signUpPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "/member/sign_up";
    }

    @PostMapping("saveMember")
    public String saveMember(MemberDto memberDto) {

        memberService.saveMember(memberDto);

        return "/member/login";
    }

    @GetMapping("login")
    public String loginPage(Model model) {
        return "member/login";
    }

    @GetMapping("login/error")
    public String loginError(Model model) {
        return "member/login";
    }


    @GetMapping("logout")
    public String logout(Model model) {return "member/login";}
}
