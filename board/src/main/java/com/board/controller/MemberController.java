package com.board.controller;

import com.board.dto.MemberDto;
import com.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("signUp")
    public String signUpPage(Model model) {

        model.addAttribute("memberDto", new MemberDto());

        return "member/sign_up";
    }

    @PostMapping("saveMember")
    public String saveMember(MemberDto memberDto) {

        memberService.saveMember(memberDto);

        return "member/login";
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

    @GetMapping("myPage")
    public String myPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "";
        } else {

            String email = auth.getName();
            model.addAttribute("member", memberService.MemberInfoByUsername(email));

            return "member/myPage";
        }

    }

    @PostMapping("infoChange")
    public String infoChange(Model model, @RequestParam String name) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "";
        } else {

            String email = auth.getName();
            memberService.updateMemberInfoByUsername(email, name);
            model.addAttribute("member", memberService.MemberInfoByUsername(email));

            return "member/myPage";
        }
    }

    @GetMapping("passwordChk")
    public String passwordChkPage(Model model) {

        return "member/passwordChk";
    }

    @PostMapping("passwordChk")
    @ResponseBody
    public boolean passwordChk(Model model, @RequestParam String password, Authentication auth) {

        String email = auth.getName();

        return memberService.passwordChkByUsername(email, password);
    }

    @GetMapping("passwordChange")
    public String passwordChangePage(Model model) {
        return "member/passwordChange";
    }

    @PostMapping("passwordChange")
    public String passwordChange(Model model, @RequestParam String password, Authentication auth) {

        String email = auth.getName();

        memberService.updatePasswordByUsername(email, password);

        return "redirect:/";
    }


}
