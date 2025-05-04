package com.example.serv1.rabbitMQListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTemplateConfig {

    private final RabbitTemplate rabbitTemplate;
    private final Jackson2JsonMessageConverter jsonMessageConverter;

    public RabbitTemplateConfig(RabbitTemplate rabbitTemplate, Jackson2JsonMessageConverter jsonMessageConverter) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonMessageConverter = jsonMessageConverter;
        this.rabbitTemplate.setMessageConverter(jsonMessageConverter);
    }
}