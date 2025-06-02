package com.example.serv1.rabbitMQListener;

import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class QueueThreeController {

    private final RabbitListenerEndpointRegistry registry;

    public QueueThreeController(RabbitListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @EventListener
    public void onReadyForQueueThree(ReadyForQueueThreeEvent event) {
        MessageListenerContainer container = registry.getListenerContainer("queue3Lis");
        if (container != null && !container.isRunning()) {
            container.start();
            System.out.println("queue3Lis STARTED");
        }
    }
}
