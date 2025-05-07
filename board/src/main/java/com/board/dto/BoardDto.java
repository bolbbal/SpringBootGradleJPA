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
    private LocalDateTime regDate; //作成時間
    private LocalDateTime modiDate; //編集時間
    private int viewCount;
    private int likeCount;

    //他の投稿情報
    private String nextTitle;
    private String prevTitle;

    private Integer nextId;
    private Integer prevId;

    //コメント保存
    private List<CommentDto> comments;
    private int commentCount;

}
