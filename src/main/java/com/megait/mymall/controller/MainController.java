package com.megait.mymall.controller;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.validation.SignUpForm;
import com.megait.mymall.service.MemberService;
import com.megait.mymall.validation.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j // log 변수
@RequiredArgsConstructor
public class MainController {

   private final MemberService memberService;
   private final MemberRepository memberRepository;

    @InitBinder("signupForm")
    protected void initinder(WebDataBinder binder){ binder.addValidators(new SignUpFormValidator(memberRepository));}

    @RequestMapping("/")
    public String index(@AuthenticationMember Member member, Model model){
        if(member != null){
            model.addAttribute(member);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signupForm, Errors errors){
        if(errors.hasErrors()){
            log.error("errors : {}",errors.getAllErrors());
            return "member/signup"; // "redirect:/signup"
        }
        log.info("올바른 회원 정보.");

        // 회원가입 서비스 실행
        Member member = memberService.processNewMember(signupForm);
        // 로그인 했다고 처리
        memberService.login(member);

        return "redirect:/"; // "/"로 리다이렉트
    }

    @GetMapping("/email-check-token")
    @Transactional
    public String emailCheckToken(String token, String email, Model model) {
        Optional<Member> optional = memberRepository.findByEmail(email);
        if (optional.isEmpty()) {
            model.addAttribute("error", "wrong.email");
            return "member/checked-email";
        }

        Member member = optional.get();
        if (!member.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return "member/checked-email";
        }

        model.addAttribute("email", member.getEmail());
        member.completeSignUp();
        return "member/checked-email";
    }
    @GetMapping("/test")
    public String test(Model model){
        model.addAttribute("name","김피카츄");
        return "test";
    }
}
