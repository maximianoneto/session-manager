package com.sicredi.sessionManager.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "result_queue";
    public static final String EXCHANGE_NAME = "result_exchange";
    public static final String ROUTING_KEY = "result_routing_key";

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(QUEUE_NAME).build();
    }


    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

}

