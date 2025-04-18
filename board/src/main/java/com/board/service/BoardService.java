package com.board.service;

import com.board.dto.BoardDto;
import com.board.dto.CommentDto;
import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.repository.BoardRepository;
import com.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public void saveBoard(Board board) {
        boardRepository.save(board);
    }

    public Page<BoardDto> listBoard(Pageable pageable, String type, String keyword) {

        Page<Board> boards;

        if(keyword == null || keyword.isEmpty()) {

            boards = boardRepository.findAllByOrderByIdDesc(pageable);

        } else if(type.equals("title") && !keyword.isEmpty()){

            boards = boardRepository.findByTitleContainingOrderByIdDesc(keyword, pageable);

        } else if(type.equals("content") && !keyword.isEmpty()) {

            boards = boardRepository.findByContentContainingOrderByIdDesc(keyword, pageable);

        } else {

            boards = boardRepository.findAllByOrderByIdDesc(pageable);

        }

        return boards.map(board -> {

            BoardDto dto = modelMapper.map(board, BoardDto.class);

            int commentCount = commentRepository.countByBoardId(board.getId());
            dto.setCommentCount(commentCount);

            return dto;
        });
    }

    public BoardDto detailBoard(int id) {

        Board board = boardRepository.findById(id);

        board.setViewCount(board.getViewCount() + 1);
        boardRepository.save(board);

        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        List<Board> prevBoard = boardRepository.findPrevBoardById(id);
        if (!prevBoard.isEmpty()) {
            Board prev = prevBoard.get(0);
            boardDto.setPrevId(prev.getId());
            boardDto.setPrevTitle(prev.getTitle());
        }

        List<Board> nextBoard = boardRepository.findNextBoardById(id);
        if (!nextBoard.isEmpty()) {
            Board next = nextBoard.get(0);
            boardDto.setNextId(next.getId());
            boardDto.setNextTitle(next.getTitle());
        }

        List<Comment> comments = board.getComments();
        List<CommentDto> commentDtos = new ArrayList<>();

        for(Comment comment : comments) {
            CommentDto commentDto = new CommentDto(comment);

            commentDtos.add(commentDto);
        }

        boardDto.setComments(commentDtos);
        boardDto.setCommentCount(comments.size());

        return boardDto;

    }

    public Board findById(int id) {
        return boardRepository.findById(id);
    }

    public Board forUpdateBoard(int id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public void updateBoard(int id, String title, String content) {

        Board board = boardRepository.findById(id);

        board.setTitle(title);
        board.setContent(content);
        board.setModiDate(LocalDateTime.now());

    }

    public void deleteBoard(int id) {
        boardRepository.deleteById(id);
    }

    public void likeCountUpdate(int id) {
        Board board = findById(id);
        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);
    }
}
