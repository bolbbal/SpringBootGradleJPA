package com.board.controller;

import com.board.dto.BoardDto;
import com.board.entity.Board;
import com.board.entity.Member;
import com.board.service.BoardService;
import com.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    @GetMapping("list/basic")
    public String listBasic(Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "type", defaultValue = "title") String type,
                            @RequestParam(value = "keyword", required = false) String keyword) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardDto> boardPage = boardService.listBoard(pageable, type, keyword);

        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("page", boardPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "board/listBasic";
    }

    @GetMapping("detail/{id}")
    public String detail(Model model, @PathVariable("id") int id) {

        BoardDto boardDto = boardService.detailBoard(id);

        model.addAttribute("board", boardDto);

        return "board/detail";
    }

    @GetMapping("write")
    @PreAuthorize("isAuthenticated()")
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

    @GetMapping("update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePage(Model model, @PathVariable("id") int id, Principal principal) {

        Board board = boardService.forUpdateBoard(id);

        if(principal == null || !principal.getName().equals(board.getWriter())) {
            return "redirect:/boards/detail/" + id;
        }

        model.addAttribute("board", board);

        return "board/update";
    }

    @PostMapping("update")
    public String updateBoard(Model model, @RequestParam("id") int id, @RequestParam("title") String title, @RequestParam("content") String content) {

        boardService.updateBoard(id, title, content);

        return "redirect:/boards/list/basic";
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable("id") int id, Principal principal) {

        Board board = boardService.forUpdateBoard(id);

        if(principal == null || !principal.getName().equals(board.getWriter())) {
            return "redirect:/boards/detail/" + id;
        }

        boardService.deleteBoard(id);
        return "redirect:/boards/list/basic";
    }

    @GetMapping("like/{id}")
    public String like(@PathVariable("id") int id) {

        boardService.likeCountUpdate(id);

        return "redirect:/boards/detail/" + id;
    }
}
