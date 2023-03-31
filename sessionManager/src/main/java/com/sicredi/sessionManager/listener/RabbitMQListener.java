package com.sicredi.sessionManager.listener;

import com.sicredi.sessionManager.config.RabbitMQConfig;
import com.sicredi.sessionManager.dto.ResultadoVotacao;
import com.sicredi.sessionManager.service.PautaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final PautaService votingResultService;

    @Autowired
    public RabbitMQListener(PautaService votingResultService) {
        this.votingResultService = votingResultService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleVotingResult(ResultadoVotacao result) {
        new ResultadoVotacao(result);
    }

}
