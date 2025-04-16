package com.board.dto;

import com.board.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private int id;
    private int boardId;
    private String comment;
    private String commenter;
    private LocalDateTime regDate;

    public CommentDto(Comment comment) {
        this.boardId = comment.getBoard().getId();
        this.comment = comment.getComment();
        this.commenter = comment.getMember().getName();
        this.regDate = comment.getRegDate();
    }

}
