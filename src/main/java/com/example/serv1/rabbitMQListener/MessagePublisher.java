package com.example.serv1.rabbitMQListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
//adaugare nota

@Service
@Slf4j
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;




    public MessagePublisher(RabbitTemplate rabbitTemplate,@Qualifier("directExchange") DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;

    }

    public boolean sendMessageStringNotes(MyMessage<String> message) {
        message.setPriority(7);

        try{
            message.setMessage("processed");

            rabbitTemplate.convertAndSend(exchange.getName(), RabbitMQConfig.ROUTING_KEY_FOUR, message);
            System.out.println(message.toString());
            return true;
        }catch (Exception e){
            return false;
        }



    }


}

