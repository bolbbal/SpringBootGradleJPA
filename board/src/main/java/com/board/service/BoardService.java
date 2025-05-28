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

        if(type.equals("title") && keyword != null){ //タイトル検索

            //タイトルでキーワードがある投稿を降順にページング照会
            boards = boardRepository.findByTitleContainingOrderByIdDesc(keyword, pageable);

        } else if(type.equals("content") && keyword != null) { //内容検索

            //内容でキーワードがある投稿を降順にページング照会
            boards = boardRepository.findByContentContainingOrderByIdDesc(keyword, pageable);

        } else { //検索ではない場合

            //投稿を降順にページング照会
            boards = boardRepository.findAllByOrderByIdDesc(pageable);

        }

        //BoardのエンティティをBoardDtoで変換
        return boards.map(board -> {

            BoardDto dto = modelMapper.map(board, BoardDto.class);

            //投稿のコメント数照会
            int commentCount = commentRepository.countByBoardId(board.getId());
            dto.setCommentCount(commentCount);

            return dto;
        });
    }

    //投稿照会時、表示する内容
    public BoardDto detailBoard(int id) {

        Board board = boardRepository.findById(id);

        //照会数１増加
        board.setViewCount(board.getViewCount() + 1);
        boardRepository.save(board);

        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        //前の投稿タイトルとID
        List<Board> prevBoard = boardRepository.findPrevBoardById(id);
        if (!prevBoard.isEmpty()) {
            Board prev = prevBoard.get(0);
            boardDto.setPrevId(prev.getId());
            boardDto.setPrevTitle(prev.getTitle());
        }

        //次の投稿タイトルとID
        List<Board> nextBoard = boardRepository.findNextBoardById(id);
        if (!nextBoard.isEmpty()) {
            Board next = nextBoard.get(0);
            boardDto.setNextId(next.getId());
            boardDto.setNextTitle(next.getTitle());
        }

        //投稿のコメント
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

    //データベースで保存された投稿を呼び出し
    public Board findById(int id) {
        return boardRepository.findById(id);
    }

    //投稿の内容変更
    @Transactional //例外発生時ロールバック
    public void updateBoard(int id, String title, String content) {

        Board board = boardRepository.findById(id);

        board.setTitle(title);
        board.setContent(content);
        board.setModiDate(LocalDateTime.now());

    }

    //投稿の削除
    public void deleteBoard(int id) {
        boardRepository.deleteById(id);
    }

    //投稿のいいね数変更
    public void likeCountUpdate(int id) {
        Board board = findById(id);
        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);
    }
}
