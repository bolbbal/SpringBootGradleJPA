package com.board.controller;

import com.board.constant.Role;
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

//掲示板関連コントローラー
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    //投稿のリスト
    @GetMapping("list/basic")
    public String listBasic(Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page, //ユーザーに見せるページの番号
                            @RequestParam(value = "size", defaultValue = "10") int size, //一つのページに見せる投稿の数
                            @RequestParam(value = "type", defaultValue = "title") String type, //検索のオプションを設定
                            @RequestParam(value = "keyword", required = false) String keyword) {

        keyword = keyword != null ? keyword.trim() : null;

        Pageable pageable = PageRequest.of(page - 1, size); //実際ページを設定
        Page<BoardDto> boardPage = boardService.listBoard(pageable, type, keyword); //該当ページの投稿リスト

        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("page", boardPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "board/listBasic";
    }

    //投稿の詳細
    @GetMapping("detail/{id}")
    public String detail(Model model, @PathVariable("id") int id) {

        BoardDto boardDto = boardService.detailBoard(id);

        model.addAttribute("board", boardDto);

        return "board/detail";
    }

    //投稿作成ページ移動
    @GetMapping("write")
    @PreAuthorize("isAuthenticated()") //ログイン有無検査
    public String writePage(Model model) {
        return "board/write";
    }

    //作成された投稿保存
    @PostMapping("write")
    public String writeBoard(Model model, @ModelAttribute BoardDto boardDto, Principal principal) {

        Member member = memberService.MemberInfoByUsername(principal.getName()); //ユーザーセッションで情報得る
        String writer = member.getName();
        Board board = modelMapper.map(boardDto, Board.class); //BoardDtoであるデータでBoardオブジェクト生成

        board.setWriter(writer);

        boardService.saveBoard(board);
        return "redirect:/boards/list/basic";
    }

    //投稿の編集ページ
    @GetMapping("update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePage(Model model, @PathVariable("id") int id, Principal principal) {

        if(principal == null) {
            return "redirect:/boards/detail/" + id;
        }
        Board board = boardService.findById(id);
        Member member = memberService.MemberInfoByUsername(principal.getName());

        //作成者検査
        if(!member.getName().equals(board.getWriter())) {
            return "redirect:/boards/detail/" + id;
        }

        model.addAttribute("board", board);

        return "board/update";
    }

    //編集された投稿保存
    @PostMapping("update")
    public String updateBoard(Model model, @RequestParam("id") int id, @RequestParam("title") String title, @RequestParam("content") String content) {

        boardService.updateBoard(id, title, content);

        return "redirect:/boards/list/basic";
    }

    //投稿の削除
    @PostMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable("id") int id, Principal principal) {

        if(principal == null) {
            return "redirect:/boards/detail/" + id;
        }

        Board board = boardService.findById(id);
        Member member = memberService.MemberInfoByUsername(principal.getName());

        //作成者と管理者が削除できる
        if(!member.getName().equals(board.getWriter()) && member.getRole() != Role.ADMIN) {
            return "redirect:/boards/detail/" + id;
        }

        boardService.deleteBoard(id);

        return "redirect:/boards/list/basic";
    }

    //投稿のいいねカウント増加
    @GetMapping("like/{id}")
    public String like(@PathVariable("id") int id) {

        boardService.likeCountUpdate(id);

        return "redirect:/boards/detail/" + id;
    }
}
