package com.example.demo.repository;

import com.example.demo.domain.Board;
import com.example.demo.domain.Member;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BoardRepository {

    private final EntityManager em;

    public BoardRepository(EntityManager em) {
        this.em = em;
    }

    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    public Board update(Board board) {
        em.merge(board);
        return board;
    }

    public Optional<Board> findById(int id) {
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }

    public List<Board> findAll() {
        List<Board> result = em.createQuery("select m from Board m", Board.class).getResultList();
        return result;
    }

    public List<Board> findAvailable() {
        List<Board> result = em.createQuery("select m from Board m where m.available = 1 order by m.id DESC", Board.class).getResultList();
        return result;
    }

}
