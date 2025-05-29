package com.board.service;

import com.board.entity.Comment;
import com.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
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

    public Comment findByComment(int id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void updateComment(int id, String modifyComment) {

        Comment comment = commentRepository.findById(id);

        comment.setComment(modifyComment);

    }
}
