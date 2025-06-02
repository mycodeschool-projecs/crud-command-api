package com.example.serv1.rabbitMQListener;

import org.springframework.context.ApplicationEvent;

public class ReadyForQueueThreeEvent extends ApplicationEvent {
    public ReadyForQueueThreeEvent(Object source) {
        super(source);
    }
}
