package com.sicredi.sessionManager.service;

import com.sicredi.sessionManager.config.RabbitMQConfig;
import com.sicredi.sessionManager.dto.ResultadoVotacao;
import com.sicredi.sessionManager.repository.PautaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PautaRepository pautaRepository;

    public void publishVotingResult(ResultadoVotacao result) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, result);
    }

}

