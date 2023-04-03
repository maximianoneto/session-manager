package com.sicredi.sessionManager.service;

import com.sicredi.sessionManager.dto.ResultadoVotacao;
import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.entity.VotoEntity;
import com.sicredi.sessionManager.exception.ResourceNotFoundException;
import com.sicredi.sessionManager.repository.PautaRepository;
import com.sicredi.sessionManager.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utils.CPFGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static utils.CPFGenerator.formatarCpf;

@Service
public class VotoService {

    @Autowired
    VotoRepository votoRepository;

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    private static final Logger logger = LoggerFactory.getLogger(VotoService.class);
    public ResponseEntity<?> registrarVoto(Long pautaId, VotoEntity voto) {
        logger.info("Iniciando registro de voto. PautaId: {}, Voto: {}", pautaId, voto);
        PautaEntity pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada. PautaId: " + pautaId));
        logger.info("Pauta encontrada. PautaId: {}", pautaId);

        String cpf = CPFGenerator.gerarCpfValido();

        String cpfFormatado = formatarCpf(cpf);

        String url = "https://cpf-validator-nextjs.vercel.app/api/cpf";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("cpf", cpfFormatado);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK && Objects.requireNonNull(response.getBody()).contains("ABLE_TO_VOTE")) {
            logger.info("CPF validado com sucesso. CPF: {}", cpfFormatado);

            boolean sessaoEncerrada = isSessaoEncerrada(pautaId);
            logger.info("Verificando se a sessão de votação está encerrada. PautaId: {}, Sessão Encerrada: {}", pautaId, sessaoEncerrada);

            if(sessaoEncerrada){
                closeVotingSession(pautaId);
                logger.info("Sessão de votação encerrada com sucesso. PautaId: {}", pautaId);
            }

            if (!sessaoEncerrada && !votoRepository.existsByPautaIdAndIdAssociado(pautaId, voto.getIdAssociado())) {
                voto.setIdAssociado(voto.getIdAssociado());
                voto.setPauta(pauta);
                voto.setCpf(CPFGenerator.desformatarCPF(cpfFormatado));
                logger.info("Voto registrado com sucesso. PautaId: {}, VotoId: {}", pautaId, voto.getIdAssociado());
                return ResponseEntity.ok(votoRepository.save(voto));
            } else {
                logger.warn("Pauta fechada ou voto já registrado. PautaId: {}, IdAssociado: {}", pautaId, voto.getIdAssociado());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERRO: 400\\nMensagem: A sessão ainda não encerrou.");
            }
        } else {
            logger.warn("CPF inválido ou não apto para votar. CPF: {}", cpfFormatado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERRO: 400\\nMensagem: CPF inválido ou não apto para votar.");
        }
    }
    public void closeVotingSession(Long pautaId) {
        logger.info("Encerrando sessão de votação para a pauta ID: {}", pautaId);
        if (!isSessaoEncerrada(pautaId)) {
            logger.warn("A sessão de votação para a pauta com ID {} ainda está aberta", pautaId);
            throw new RuntimeException("A sessão de votação para a pauta com ID " + pautaId + " ainda está aberta");
        }
        ResultadoVotacao result = contabilizarVotos(pautaId);
        rabbitMQPublisher.publishVotingResult(result);
        logger.info("Resultado da votação para pauta com ID {} publicado no RabbitMQ: {}", pautaId, result);
    }
    boolean isSessaoEncerrada(Long pautaId) {
        logger.info("Verificando se a sessão de votação para a pauta com ID {} encerrou", pautaId);
        PautaEntity pauta = pautaRepository.findById(pautaId).orElse(null);

        if (pauta == null || pauta.getDataHoraInicioSessao() == null || pauta.getDuracaoSessao() == null) {
            logger.warn("Pauta, hora da sessão ou duração da sessão não encontradas. PautaId: {}", pautaId);
            return false;
        }

        LocalDateTime horaSessao = pauta.getDataHoraInicioSessao();
        Duration duracaoSessao = pauta.getDuracaoSessao();
        LocalDateTime agora = LocalDateTime.now();

        // Adiciona a duração da sessão à hora de início da sessão
        LocalDateTime horaEncerramentoSessao = horaSessao.plus(duracaoSessao);

        // Verifica se o horário atual é posterior ao horário de encerramento da sessão
        boolean sessaoEncerrada = agora.isAfter(horaEncerramentoSessao);
        logger.info("A sessão de votação para a pauta com ID {} encerrou: {}", pautaId, sessaoEncerrada);
        return sessaoEncerrada;
    }

    public ResultadoVotacao contabilizarVotos(Long pautaId) {
        logger.info("Calculando o resultado da votação para a pauta com ID {}", pautaId);
        PautaEntity pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada. PautaId: " + pautaId));
        List<VotoEntity> votos = votoRepository.findByPauta(pauta);
        long votosSim = votos.stream().filter(VotoEntity::isVoto).count();
        long votosNao = votos.size() - votosSim;
        logger.info("Resultado da votação para a pauta com ID {}: {} votos SIM e {} votos NÃO", pautaId, votosSim, votosNao);
        return new ResultadoVotacao(votosSim, votosNao);
    }
}
