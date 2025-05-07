package com.board.repository;

import com.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    //該当投稿のコメントリスト
    List<Comment> findByBoardId(int boardId);

    //該当投稿のコメント数
    int countByBoardId(int boardId);

    //該当ユーザーが作成したコメントの削除
    void deleteByMemberId(Long memberId);
}
