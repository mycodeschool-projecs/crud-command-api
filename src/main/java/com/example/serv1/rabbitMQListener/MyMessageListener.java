package com.example.serv1.rabbitMQListener;
//import com.example.commandservice.model.MapStocOptim;
//import com.example.commandservice.service.MapStocServiceImpl;
import com.example.serv1.model.MyClient;
import com.example.serv1.model.Note;
import com.example.serv1.repository.NoteRepository;
import com.example.serv1.services.MyClientServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MyMessageListener{

    private MyClientServices myClientServices;
    private NoteRepository noteRepository;

    public MyMessageListener(MyClientServices myClientServices,NoteRepository noteRepository){
        this.myClientServices=myClientServices;
        this.noteRepository=noteRepository;
    }

    @RabbitListener(queues = ConsumerConfig.QUEUE_ONE)
    public void receiveMessageString(MyMessage<String> message) {
//
//        if(message.getMessage().trim().equals("DEL_PROD_ID")){
//            mapStocService.delMapStoc(message.getContent().trim());
//            log.info("A mers stergerea");
//
//            System.out.println("Received message: " +message.toString());
//
//        }

    }


    @RabbitListener(queues = ConsumerConfig.QUEUE_TWO)
    public void receiveMessageMyClient(MyMessage<MyClient> message) throws Exception {
        Note note=new Note();
        note.setLoggedClient(null);
        note.setOperation(message.getMessage());
        LocalDateTime time = LocalDateTime.now();
        note.setLogTime(time);
        note.setEmail(message.getContent().getEmail());
        noteRepository.save(note);
        if(message.getMessage().trim().equals("ADD_CLIENT")){
            try {
                myClientServices.addClient(message.getContent());
                Optional oNote=noteRepository.findByLogTimeaAndEmail(time,message.getContent().getEmail());
                if(oNote.isPresent()){
                    noteRepository.delete((Note)oNote.get());
                }
                log.info("Add Client "+message.toString());
            }catch (Exception e){
                log.error("ADD_CLIENT_FAIL");
                throw new Exception("ADD_CLIENT_FAIL");
            }

        }

        if(message.getMessage().trim().equals("UPD_CLIENT")){
            try {
                myClientServices.updClient(message.getContent());
                Optional oNote=noteRepository.findByLogTimeaAndEmail(time,message.getContent().getEmail());
                if(oNote.isPresent()){
                    noteRepository.delete((Note)oNote.get());
                }

                log.info("UPD Client "+message.toString());
            }catch (Exception e){
                log.error("UPD_CLIENT_FAIL");
                throw new Exception("UPD_CLIENT_FAIL");
            }


        }

        if(message.getMessage().trim().equals("DEL_CLIENT")){
            try {
                myClientServices.delClient(message.getContent().getEmail());

                Optional oNote=noteRepository.findByLogTimeaAndEmail(time,message.getContent().getEmail());
                if(oNote.isPresent()){
                    noteRepository.delete((Note)oNote.get());
                }

                log.info("DEL Client "+message.toString());
            }catch (Exception e){
                log.error("DEL_CLIENT_FAIL");
                throw new Exception("DEL_CLIENT_FAIL");

            }


        }

    }


//    @RabbitListener(queues = ConsumerConfig.QUEUE_TWO)
//    public void receiveMessageMapStocOpt(MyMessage<Note> message) {
//
//        if(message.getMessage().trim().equals("UPD_PROD")){
//            mapStocService.updMapStoc(message.getContent());
//            log.info("A mers stergerea");
//
//            System.out.println("Received message: " +message.toString());
//
//        }
//    }

}
