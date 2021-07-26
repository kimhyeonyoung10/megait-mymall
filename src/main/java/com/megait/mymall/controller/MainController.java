package com.megait.mymall.controller;

import com.megait.mymall.domain.Member;
import com.megait.mymall.validation.SignUpForm;
import com.megait.mymall.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j // log 변수
public class MainController {

    @Autowired
    MemberService memberService;
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/signup")
    public String signUpForm(){
        return "member/signup";
    }
    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signupForm, Errors errors){
        if(errors.hasErrors()){
            log.info("유효하지 않은 회원 정보. 가입 불가");
            return "member/signup"; // "redirect:/signup"
        }
        log.info("올바른 회원 정보.");

        // 회원가입 서비스 실행
        Member member = memberService.processNewMember(signupForm);
        // 로그인 했다고 처리
        memberService.login(member);

        return "redirect:/"; // "/"로 리다이렉트
    }
}
