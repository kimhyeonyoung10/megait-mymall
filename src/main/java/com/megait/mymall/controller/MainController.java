package com.megait.mymall.controller;

import com.google.gson.JsonObject;
import com.megait.mymall.domain.Item;
import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.OrderItem;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.service.ItemService;
import com.megait.mymall.service.OrderService;
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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j // log 변수
@RequiredArgsConstructor
public class MainController {

   private final MemberService memberService;
   private final MemberRepository memberRepository;
   private final ItemService itemService;
   private final OrderService orderService;

    @InitBinder("signupForm")
    protected void initinder(WebDataBinder binder){ binder.addValidators(new SignUpFormValidator(memberRepository));}

    @RequestMapping("/")
    public String index(@AuthenticationMember Member member, Model model){

        model.addAttribute("bookList", itemService.getBookList());
        model.addAttribute("albumList",itemService.getAlbumList());

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
    @GetMapping("/item/detail/{id}") //PathVariable 꼭 설정해줘야함
    public String detail(@PathVariable Long id, Model model){
        Item item = itemService.getItem(id);
        model.addAttribute("item",item);
        return "item/detail";
    }
    @ResponseBody
    @GetMapping("/item/like/{id}")
    public String likeItem(@AuthenticationMember Member member, @PathVariable Long id) {
        JsonObject object = new JsonObject();
        if(member == null){
            object.addProperty("result", false);
            object.addProperty("message", "로그인이 필요한 기능입니다.");
            return object.toString();
        }
        try {
            itemService.addLike(member, id);
            object.addProperty("result", true);
            object.addProperty("message", "찜 목록에 등록되었습니다.");

        } catch (IllegalArgumentException e){
            object.addProperty("result", false);
            object.addProperty("message", e.getMessage());
        }
        return object.toString();
    }

    @GetMapping("/item/like-list")
    public String likeList(@AuthenticationMember Member member, Model model){
        List<Item> likeList = memberService.getLikeList(member);
        model.addAttribute("likeList",likeList);
        return "item/like_list";
    }
    @PostMapping("/cart/list")
    public String addCart(@AuthenticationMember Member member,
                          @RequestParam("item_id") String[] itemIds,
                          Model model){

        //List<Long> idList = List.of(Arrays.stream(itemIds).map(Long::parseLong).toArray(Long[]::new));

        // String[] ~> List<Long>

        List<Long> idList = Arrays.stream(itemIds).map(Long::parseLong).collect(Collectors.toList());
        orderService.addCart(member, idList);
        itemService.deleteLikes(member, idList);

        return cartList(member, model);
    }
    @GetMapping("/cart/list")
    public String cartList(@AuthenticationMember Member member, Model model){

        try {
            List<OrderItem> cartList = orderService.getCart(member);
            model.addAttribute("cartList", cartList);
            model.addAttribute("totalPrice", orderService.getTotalPrice(cartList));

        } catch (IllegalStateException e){
            model.addAttribute("error_message", e.getMessage());
        }
        return "cart/list";
    }
    @PostMapping("/cart/delete")
    public String cartDelete(@AuthenticationMember Member member,
                             @RequestParam(value = "item_id", required = false) Long[] itemIds,
                             Model model){
        if(itemIds != null && itemIds.length != 0){
            List<Long> idList = List.of(itemIds);
            orderService.deleteCart(idList);
        }
        return cartList(member, model);
    }
}
