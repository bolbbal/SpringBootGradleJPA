package com.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board") //データベースで「board」テーブル生成
@NoArgsConstructor
@Data
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自動的に１づつ増加
    private int id;

    private String title;

    @Lob
    private String content;
    private String writer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDate;

    private LocalDateTime modiDate;

    private int viewCount;
    private int likeCount;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) //関係設定
    @OrderBy("id asc")
    private List<Comment> comments = new ArrayList<>();

    @PrePersist //生成時、「viewcount」と「likecount」を０で設定
    public void prePersist() {
        this.viewCount = 0;
        this.likeCount = 0;
    }
}
