package com.sicredi.sessionManager.service;

import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PautaService {
    @Autowired
    private  PautaRepository pautaRepository;

    public PautaEntity criarPauta(PautaEntity pauta) {
        return pautaRepository.save(pauta);
    }

    public PautaEntity abrirSessao(Long pautaId, Duration duracao) {
        PautaEntity pauta = pautaRepository.findById(pautaId).orElseThrow(); // Tratar exceção
        pauta.setSessaoAberta(true);
        pauta.setDuracaoSessao(duracao);
        pauta.setDataHoraInicioSessao(LocalDateTime.now());
        return pautaRepository.save(pauta);
    }

}
