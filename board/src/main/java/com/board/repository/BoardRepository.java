package com.board.repository;

import com.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //投稿を降順にページング照会
    Page<Board> findAllByOrderByIdDesc(Pageable pageable);

    //タイトルでキーワードがある投稿を降順にページング照会
    Page<Board> findByTitleContainingOrderByIdDesc(String keyword, Pageable pageable);

    //内容でキーワードがある投稿を降順にページング照会
    Page<Board> findByContentContainingOrderByIdDesc(String keyword, Pageable pageable);

    //該当IDの投稿詳細照会
    Board findById(int id);

    //該当IDの投稿の以前投稿
    @Query("select b from Board b where b.id < :id order by b.id desc")
    List<Board> findPrevBoardById(int id);

    //該当IDの投稿の以後投稿
    @Query("select b from Board b where b.id > :id order by b.id asc")
    List<Board> findNextBoardById(int id);

}
