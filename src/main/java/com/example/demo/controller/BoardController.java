package com.example.demo.controller;

import com.example.demo.domain.Board;
import com.example.demo.domain.Member;
import com.example.demo.service.BoardService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
@SessionAttributes("member")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @ModelAttribute("member")
    public Member setEmptyMember() {
        return new Member();
    }

    @GetMapping("/board/board")
    public String listView(Model model) {
        List<Board> boardList = boardService.viewList();
        model.addAttribute("boardList", boardList);
        return "/board/board";
    }

    @GetMapping("/board/write")
    public String writeForm(@ModelAttribute("member") Member member) {
        if(member.getName() == null)
            return "redirect:/board/board";
        return "/board/write";
    }

    @PostMapping("/board/write")
    public String write(BoardForm form) {
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setMemberName(form.getMemberName());
        board.setMemberId(form.getMemberId());

        boardService.write(board);
        return "redirect:/board/board";
    }

    @GetMapping("/board/view")
    public String view(@RequestParam("no") int id, Model model) {

        try {
            Board board = boardService.view(id);
            model.addAttribute("article", board);
        }
        catch (IllegalStateException e) {
            model.addAttribute("err", e.getMessage());
            return "redirect:/board/board";
        }

        return "/board/view";
    }

    @GetMapping("/board/edit")
    public String editForm(@RequestParam("no") int id, @ModelAttribute("member") Member member, Model model) {
        if(member.getName() == null)
            return "redirect:/board/board";
        try {
            Board board = boardService.editForm(id, member.getId());
            model.addAttribute("article", board);
        } catch (IllegalStateException e) {
            model.addAttribute("err", e.getMessage());
            return "redirect:/board/board";
        }

        return "/board/edit";
    }

    @PostMapping("/board/edit")
    public String edit(BoardForm form) {
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setId(form.getId());
        board.setContent(form.getContent());
        board.setCount(form.getCount());
        board.setMemberName(form.getMemberName());
        board.setMemberId(form.getMemberId());

        boardService.edit(board);
        return "redirect:/board/board";
    }

    @PostMapping("/board/delete")
    public String delete(int no) {
        boardService.delete(no);
        return "redirect:/board/board";
    }

}
