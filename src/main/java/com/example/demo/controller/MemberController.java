package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("member")
public class MemberController {
    private  final MemberService memberService;

    @ModelAttribute("member")
    public Member setEmptyMember() {
        return new Member();
    }

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/join")
    public String joinMemberForm() {
        return "members/join";
    }

    @PostMapping("/members/join")
    public String joinMember(MemberForm form, Model model) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());

        try {
            memberService.join(member);
            model.addAttribute("member", member);
        } catch (IllegalStateException e) {
            model.addAttribute("err", e.getMessage());
            return "/members/join";
        }

        return "redirect:/";
    }

    @GetMapping("/members/login")
    public String loginMemberForm(@ModelAttribute("member") Member member) {
        if(member.getName() != null)
            return "redirect:/";

        return "members/login";
    }

    @GetMapping("/members/logout")
    public String logout(SessionStatus session) {
        session.setComplete();
        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(MemberForm form, Model model) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());

        try {
            Member rs = memberService.login(member);
            model.addAttribute("member", rs);
        }
        catch (IllegalStateException e) {
            model.addAttribute("err", e.getMessage());
            return "/members/login";
        }

        return "redirect:/";
    }

}
