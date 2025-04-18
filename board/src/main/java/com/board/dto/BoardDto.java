package com.board.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    private String nextTitle;
    private String prevTitle;

    private Integer nextId;
    private Integer prevId;

    private List<CommentDto> comments;
    private int commentCount;

}
