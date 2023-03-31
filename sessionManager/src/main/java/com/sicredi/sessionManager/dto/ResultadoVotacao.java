package com.sicredi.sessionManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadoVotacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private long votosSim;
    private long votosNao;

    public ResultadoVotacao(ResultadoVotacao resultadoVotacao) {
        resultadoVotacao.votosNao = votosNao;
        resultadoVotacao.votosSim = votosSim;
    }

    public ResultadoVotacao(long votosSim, long votosNao) {
        this.votosSim = votosSim;
        this.votosNao = votosNao;
    }

    public ResultadoVotacao() {
    }
}

