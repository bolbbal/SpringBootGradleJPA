package com.board.controller;

import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.entity.Member;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

//コメント関連コントローラー
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final BoardService boardService;

    //コメント保存
    @GetMapping("save")
    @PreAuthorize("isAuthenticated()")//ログイン有無
    private String saveComment(Model model, @RequestParam("boardId") int boardId, @RequestParam("comment") String comments, Principal principal) {

        Member member = memberService.MemberInfoByUsername(principal.getName());
        Board board = boardService.findById(boardId);

        Comment comment = new Comment();

        comment.setComment(comments);
        comment.setBoard(board);
        comment.setMember(member);

        commentService.saveComment(comment);

        return "redirect:/boards/detail/" + boardId;
    }

}
