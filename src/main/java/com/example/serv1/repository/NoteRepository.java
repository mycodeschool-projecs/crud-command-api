package com.example.serv1.repository;

import com.example.serv1.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query(value = "select n from Note n where n.email=?1 and n.operation=?2")
    List<Note> findAllByEmailAndOperation(String email, String operation);

    @Query(value = "select n from Note n where n.logTime=?1 and n.email=?2")
    Optional<Note> findByLogTimeaAndEmail(LocalDateTime logTime, String email);

}
