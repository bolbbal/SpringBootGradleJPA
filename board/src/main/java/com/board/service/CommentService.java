package com.board.service;

import com.board.entity.Comment;
import com.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    //コメント保存
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
