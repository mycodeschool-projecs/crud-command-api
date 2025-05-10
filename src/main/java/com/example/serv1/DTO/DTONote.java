package com.example.serv1.DTO;


import lombok.*;

import java.time.LocalDateTime;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DTONote {

    private long id;
    private long client;
    private long loggedUser;
    private String email;
    private String operation;
    private LocalDateTime logTime;
    private NoteStatus status;

}
