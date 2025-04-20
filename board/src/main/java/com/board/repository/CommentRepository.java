package com.board.repository;

import com.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByBoardId(int boardId);

    int countByBoardId(int boardId);

    void deleteByMemberId(Long memberId);
}
