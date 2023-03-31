package com.sicredi.sessionManager.service;

import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.exception.ResourceNotFoundException;
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
        if(pautaId == null || duracao == null) {
            throw new IllegalArgumentException("PautaId e duração são obrigatórios");
        }
        PautaEntity pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada. PautaId: " + pautaId));
        pauta.setSessaoAberta(true);
        pauta.setDuracaoSessao(duracao);
        pauta.setDataHoraInicioSessao(LocalDateTime.now());
        return pautaRepository.save(pauta);
    }

}
