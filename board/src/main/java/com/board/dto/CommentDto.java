package com.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private int id;
    private int board_id;
    private String comment;
    private String commenter;
    private LocalDateTime regDate;

}
