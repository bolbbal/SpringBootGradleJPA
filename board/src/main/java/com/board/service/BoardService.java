package com.board.service;

import com.board.dto.BoardDto;
import com.board.entity.Board;
import com.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public void saveBoard(Board board) {
        boardRepository.save(board);
    }

    public Page<BoardDto> listBoard(Pageable pageable) {

        Page<Board> boards;

        boards = boardRepository.findAllByOrderByRegDateDesc(pageable);

        return boards.map(board -> {

            BoardDto dto = modelMapper.map(board, BoardDto.class);

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

        return boardDto;

    }

    public Board forUpdateBoard(int id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public void updateBoard(int id, String title, String content) {

        Board board = boardRepository.findById(id);

        board.setTitle(title);
        board.setContent(content);

    }

    public void deleteBoard(int id) {
        boardRepository.deleteById(id);
    }
}
