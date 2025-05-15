package com.example.serv1.rabbitMQListener;
//import com.example.commandservice.model.MapStocOptim;
//import com.example.commandservice.service.MapStocServiceImpl;
import com.example.serv1.model.MyClient;
import com.example.serv1.services.MyClientServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyMessageListener{

    private MyClientServices myClientServices;

    private volatile boolean readyForQueueThree = false;
    private String id_note="";


    private final RabbitListenerEndpointRegistry registry;

    public MyMessageListener(MyClientServices myClientServices,
                             RabbitListenerEndpointRegistry registry){

        this.registry=registry;
        this.myClientServices=myClientServices;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ONE)
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




    @RabbitListener(id="queue3Lis",queues = RabbitMQConfig.QUEUE_THREE)
    public void receiveMessageStringNotes(MyMessage<String> message) {
//
        if (readyForQueueThree) {
            log.info("QUEUE_THREE: {}", message);
            String idNote = message.getMessage();
            // după procesare, resetezi
            readyForQueueThree = false;
            myClientServices.sendMessageNote(idNote);
            registry.getListenerContainer("queue3Lis").stop();

        } else {
            log.warn("QUEUE_THREE message primit, dar încă nu e permis. Ignorat sau requeue.");
            // opțional: aruncă excepție ca să fie retrimis în coadă
            throw new RuntimeException("Not ready for QUEUE_THREE");
        }


    }


    @RabbitListener(id="queue2Lis",queues = RabbitMQConfig.QUEUE_TWO)
    public void receiveMessageMyClient(MyMessage<MyClient> message) throws Exception {
//
        if(message.getMessage().trim().equals("ADD_CLIENT")){
            try {
                myClientServices.addClient(message.getContent());
                readyForQueueThree=true;

                registry.getListenerContainer("queue3Lis").start();
                log.info("Add Client "+message.toString());
            }catch (Exception e){
                log.error("ADD_CLIENT_FAIL");
                throw new Exception("ADD_CLIENT_FAIL");
            }

        }

        if(message.getMessage().trim().equals("UPD_CLIENT")){
            try {
                myClientServices.updClient(message.getContent());


                log.info("UPD Client "+message.toString());
            }catch (Exception e){
                log.error("UPD_CLIENT_FAIL");
                throw new Exception("UPD_CLIENT_FAIL");
            }


        }

        if(message.getMessage().trim().equals("DEL_CLIENT")){
            try {
                myClientServices.delClient(message.getContent().getEmail());



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
