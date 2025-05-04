package com.example.serv1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity(name = "Note")
@Table(name = "notes")
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "note_generator")
    @SequenceGenerator(name = "note_generator",initialValue = 1,allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "myclient_id") // coloana FK în tabela
    private MyClient loggedClient;

    private String email;
    private String operation;
    private LocalDateTime logTime;
    

}
