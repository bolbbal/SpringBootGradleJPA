package com.board.controller;

import com.board.dto.BoardDto;
import com.board.entity.Board;
import com.board.entity.Member;
import com.board.service.BoardService;
import com.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    @GetMapping("list/basic")
    public String listBasic(Model model, @PageableDefault(size = 10) Pageable pageable) {

        Page<BoardDto> boardPage = boardService.listBoard(pageable);
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("page", boardPage);
        return "board/listBasic";
    }

    @GetMapping("detail")
    public String detail(Model model) {
        return "board/detail";
    }

    @GetMapping("write")
    public String writePage(Model model) {
        return "board/write";
    }

    @PostMapping("write")
    public String writeBoard(Model model, @ModelAttribute BoardDto boardDto, Principal principal) {

        Member member = memberService.MemberInfoByUsername(principal.getName());
        String writer = member.getName();
        Board board = modelMapper.map(boardDto, Board.class);

        board.setWriter(writer);

        boardService.saveBoard(board);
        return "redirect:/boards/list/basic";
    }

    @GetMapping("update")
    public String updatePage(Model model) {
        return "board/update";
    }

    @PostMapping("update")
    public String updateBoard(Model model) {
        return "redirect:/boards/list/basic";
    }

    @PostMapping("delete")
    public String delete(Model model) {
        return "";
    }
}
