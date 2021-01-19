package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.domain.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board write(Board board) {
        board.setDate(LocalDateTime.now());
        board.setAvailable(true);
        board.setCount(0);

        int size = boardRepository.findAll().size();
        if(size==0) {
            board.setId(1);
        }
        else {
            board.setId(size+1);
        }
        boardRepository.save(board);
        return board;
    }

    public Board edit(Board board) {
        board.setDate(LocalDateTime.now());
        board.setAvailable(true);

        boardRepository.update(board);
        return board;
    }

    public void delete(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isPresent()) {
            board.get().setAvailable(false);
            boardRepository.update(board.get());
        }
        else
            throw new IllegalStateException("존재하지 않는 글입니다.");
    }

    public List<Board> viewList() {
        return boardRepository.findAvailable();
    }

    public Board view(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isPresent()) {
            if(board.get().isAvailable()) {
                //조회수 1증가
                board.get().setCount(board.get().getCount()+1);
                boardRepository.update(board.get());

                board.get().setContent(board.get().getContent().replace("\r\n","<br />"));
                return board.get();
            }
            else
                throw new IllegalStateException("삭제된 글입니다.");
        } else
            throw new IllegalStateException("존재하지 않는 글입니다.");

    }

    public Board editForm(int id, String memberId) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isPresent()) {
            if(board.get().isAvailable()) {
                if(board.get().getMemberId().equals(memberId)) {
                    board.get().setContent(board.get().getContent().replace("\r\n", "<br />"));
                    return board.get();
                }
                else
                    throw new IllegalStateException("편집 권한이 없습니다.");
            }
            else
                throw new IllegalStateException("삭제된 글입니다.");
        } else
            throw new IllegalStateException("존재하지 않는 글입니다.");

    }

}
