package com.board.controller;

import com.board.dto.MemberDto;
import com.board.entity.Member;
import com.board.service.MemberService;
import com.board.util.MailSenderRunner;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MailSenderRunner mailSenderRunner;

    @GetMapping("signUp")
    public String signUpPage(Model model) {

        model.addAttribute("memberDto", new MemberDto());

        return "member/sign_up";
    }

    private String certify = "";

    @PostMapping("emailSend")
    @ResponseBody
    public Map<String, Boolean> emailSend(Model model, @RequestParam("email") String email) {

        Map<String, Boolean> result = new HashMap<>();

        boolean isExist = false;

        Member member = memberService.findByEmail(email);

        if(member == null) {
            isExist = true;

            certify = mailSenderRunner.sendMail(email);
        }

        System.out.println(certify);

        result.put("success", isExist);

        return result;

    }

    @PostMapping("certifyChk")
    @ResponseBody
    public Map<String, Boolean> certifyChk(Model model, @RequestParam("certifyChk") int certifyChk) {

        Map<String, Boolean> result = new HashMap<>();

        boolean isTrue = false;

        String certifyNum = Integer.toString(certifyChk);

        if(certify.equals(certifyNum)) {
            isTrue = true;
        }

        result.put("success", isTrue);

        return result;

    }

    @PostMapping("saveMember")
    public String saveMember(@Valid MemberDto memberDto, BindingResult bindingResult) {

       if(bindingResult.hasErrors()) {
           return "member/sign_up";
       }

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

        return "redirect:/members/logout";
    }

    @PostMapping("delete")
    @ResponseBody
    public boolean deleteMember(Authentication auth) {

        return memberService.deleteMember(auth.getName());
    }


}
