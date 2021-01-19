package com.example.demo.controller;

import com.example.demo.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("member")
public class HomeController {

    @ModelAttribute("member")
    public Member setEmptyMember() {
        return new Member();
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
