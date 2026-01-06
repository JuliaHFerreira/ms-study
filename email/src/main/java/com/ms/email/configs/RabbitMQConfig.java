package com.ms.email.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.email.name}") // pega a propriedade do application.yaml
    private String queue;

    @Bean
    public Queue queue(){
        return new Queue(queue, true); // cria a fila, durável
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

//    O que é Jackson2JsonMessageConverter?
//    É um conversor de mensagens do Spring AMQP que usa o Jackson para:
    //    Enviar: transformar um objeto Java → JSON antes de colocar na fila.
    //    Receber: transformar o JSON → objeto Java quando o listener recebe a mensagem.
}

