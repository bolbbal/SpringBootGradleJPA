package com.board.service;

import com.board.dto.BoardDto;
import com.board.entity.Board;
import com.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Page<BoardDto> listBoard(Pageable pageable) {

        Page<Board> boards;

        boards = boardRepository.findAllByOrderByRegDateDesc(pageable);

        return boards.map(board -> {

            BoardDto dto = modelMapper.map(board, BoardDto.class);

            return dto;
        });
    }
}
