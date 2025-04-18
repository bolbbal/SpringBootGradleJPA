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

    Page<Board> findAllByOrderByIdDesc(Pageable pageable);

    Page<Board> findByTitleContainingOrderByIdDesc(String keyword, Pageable pageable);

    Page<Board> findByContentContainingOrderByIdDesc(String keyword, Pageable pageable);

    Board findById(int id);

    @Query("select b from Board b where b.id < :id order by b.id desc")
    List<Board> findPrevBoardById(int id);

    @Query("select b from Board b where b.id > :id order by b.id asc")
    List<Board> findNextBoardById(int id);

}
