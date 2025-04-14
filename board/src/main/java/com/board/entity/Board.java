package com.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="board")
@NoArgsConstructor
@Data
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Lob
    private String content;
    private String writer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime modiDate;

    private int viewCount;
    private int likeCount;

    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
        this.likeCount = 0;
    }
}
