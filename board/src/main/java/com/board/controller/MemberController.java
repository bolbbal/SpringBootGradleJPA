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

import java.util.HashMap;
import java.util.Map;

//ユーザー関連コントローラー
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MailSenderRunner mailSenderRunner;

    //新規登録ページ
    @GetMapping("signUp")
    public String signUpPage(Model model) {

        model.addAttribute("memberDto", new MemberDto());

        return "member/sign_up";
    }

    //認証番号保存
    private String certify = "";

    //認証番号送信
    @PostMapping("emailSend")
    @ResponseBody
    public Map<String, Boolean> emailSend(Model model, @RequestParam("email") String email) {

        Map<String, Boolean> result = new HashMap<>();

        boolean isExist = false;

        Member member = memberService.findByEmail(email); //データベースで入力されたメール検査

        if(member == null) {
            isExist = true;

            certify = mailSenderRunner.sendMail(email); //認証番号送信
        }

        System.out.println(certify);

        result.put("success", isExist);

        return result;

    }

    //認証番号検査
    @PostMapping("certifyChk")
    @ResponseBody
    public Map<String, Boolean> certifyChk(Model model, @RequestParam("certifyChk") int certifyChk) {

        Map<String, Boolean> result = new HashMap<>();

        boolean isTrue = false;

        String certifyNum = Integer.toString(certifyChk);

        //送信した認証番号と入力された認証番号の一致有無確認
        if(certify.equals(certifyNum)) {
            isTrue = true;
        }

        result.put("success", isTrue);

        return result;

    }

    //ユーザー情報保存
    @PostMapping("saveMember")
    public String saveMember(@Valid MemberDto memberDto, BindingResult bindingResult) {

       if(bindingResult.hasErrors()) {
           return "member/sign_up";
       }

        memberService.saveMember(memberDto);

        return "member/login";
    }

    //ログインページ
    @GetMapping("login")
    public String loginPage(Model model) {
        return "member/login";
    }

    //ログイン失敗時
    @GetMapping("login/error")
    public String loginError(Model model) {
        return "member/login";
    }

    //マイページ
    @GetMapping("myPage")
    public String myPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        //セッションでユーザーの情報検索
        model.addAttribute("member", memberService.MemberInfoByUsername(email));

        return "member/myPage";
    }

    //変わったユーザー情報保存
    @PostMapping("infoChange")
    public String infoChange(Model model, @RequestParam String name) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        memberService.updateMemberInfoByUsername(email, name);
        model.addAttribute("member", memberService.MemberInfoByUsername(email));

        return "member/myPage";

    }

    //パスワード検査ページ
    @GetMapping("passwordChk")
    public String passwordChkPage(Model model) {

        return "member/passwordChk";
    }

    //パスワード検査実行
    @PostMapping("passwordChk")
    @ResponseBody
    public boolean passwordChk(Model model, @RequestParam String password, Authentication auth) {

        String email = auth.getName();

        return memberService.passwordChkByUsername(email, password);
    }

    //パスワード編集ページ
    @GetMapping("passwordChange")
    public String passwordChangePage(Model model) {
        return "member/passwordChange";
    }

    //パスワード編集
    @PostMapping("passwordChange")
    public String passwordChange(Model model, @RequestParam String password, Authentication auth) {

        String email = auth.getName();

        memberService.updatePasswordByUsername(email, password);

        return "redirect:/members/logout";
    }

    //会員退会
    @PostMapping("delete")
    @ResponseBody
    public boolean deleteMember(Authentication auth) {

        return memberService.deleteMember(auth.getName());
    }


}
