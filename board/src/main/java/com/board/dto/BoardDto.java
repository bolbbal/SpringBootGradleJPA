package com.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDto {

    private int id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime modiDate;
    private int viewCount;
    private int likeCount;

}
