package com.board.controller;

import com.board.constant.Role;
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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("modifyChk/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    private String modifyCommentChk(Model model, @PathVariable("id") int id, @RequestParam("boardId") int boardId, Principal principal) {

        String result = "";

        if(principal == null) {
            return "redirect:/boards/detail/" + boardId;
        }

        Comment comment = commentService.findByComment(id);
        Member member = memberService.MemberInfoByUsername(principal.getName());

        if(member != comment.getMember()) {
            return "redirect:/boards/detail/" + boardId;
        }

        result = "possible";

        return result;

    }

    @GetMapping("modify/{id}")
    @PreAuthorize("isAuthenticated()")
    private String modifyComment(Model model, @PathVariable("id") int id, @RequestParam("boardId") int boardId, @RequestParam("modifyComment") String modifyComment, Principal principal) {

        if(principal == null) {
            return "redirect:/boards/detail/" + boardId;
        }

        Comment comment = commentService.findByComment(id);
        Member member = memberService.MemberInfoByUsername(principal.getName());

        if(member == comment.getMember()) {
            commentService.updateComment(id, modifyComment);
        }

        return "redirect:/boards/detail/" + boardId;

    }

    @PostMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable("id") int id, @RequestParam("boardId") int boardId, Principal principal) {

        if(principal == null) {
            return "redirect:/boards/detail/" + boardId;
        }

        Comment comment = commentService.findByComment(id);
        System.out.println(id);
        System.out.println(comment);
        Member member = memberService.MemberInfoByUsername(principal.getName());

        //作成者と管理者が削除できる
        if(member != comment.getMember() && member.getRole() != Role.ADMIN) {
            return "redirect:/boards/detail/" + boardId;
        }

        commentService.deleteComment(id);

        return "redirect:/boards/detail/" + boardId;
    }


}
