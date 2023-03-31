package com.sicredi.sessionManager.controller;

import com.sicredi.sessionManager.dto.ResultadoVotacao;
import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.entity.VotoEntity;
import com.sicredi.sessionManager.repository.PautaRepository;
import com.sicredi.sessionManager.service.PautaService;
import com.sicredi.sessionManager.service.VotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/api/pautas")
@Api(value = "API de Gerenciamento de Sessão", tags = { "Pautas" })
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private PautaService pautaService;

    @Autowired
    private VotoService votoService;

    @PostMapping("/name/")
    @ApiOperation(value = "Criar uma nova pauta", response = PautaEntity.class)
    public ResponseEntity<PautaEntity> criarPauta(@RequestBody @ApiParam(value = "Nome da Pauta", required = true) PautaEntity pauta) {
        PautaEntity pautaSalva = pautaService.criarPauta(pauta);
        return ResponseEntity.ok(pautaSalva);
    }
    @PostMapping("/{pautaId}/abrir-sessao")
    @ApiOperation(value = "Abrir uma sessão de votação para uma pauta", response = PautaEntity.class)
    public ResponseEntity<PautaEntity> abrirSessao(@PathVariable Long pautaId,
                                             @RequestParam(value = "duracaoSessao", required = false,defaultValue = "PT1M")
                                             @ApiParam(value = "Duração da sessão de votação, em formato ISO 8601 (ex: PT1M para 1 minuto)", defaultValue = "PT1M") String duracao) {
        Duration duracao1 = Duration.parse(duracao);
        if (duracao1 == null) {
            duracao1 = Duration.ofMinutes(1);
        }
        PautaEntity pauta = pautaService.abrirSessao(pautaId, duracao1);
        return ResponseEntity.ok(pauta);
    }
    @PostMapping("/{pautaId}/votar")
    @ApiOperation(value = "Registrar um voto em uma pauta", response = VotoEntity.class)
    public ResponseEntity<VotoEntity> registrarVoto(@PathVariable Long pautaId,
                                                    @RequestBody @ApiParam(value = "Voto a ser registrado", required = true) VotoEntity voto) {
        ResponseEntity<?> votoRegistrado = votoService.registrarVoto(pautaId, voto);
        return (ResponseEntity<VotoEntity>) votoRegistrado;
    }
    @GetMapping(value = "/{pautaId}/resultado",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Obter o resultado da votação de uma pauta", response = ResultadoVotacao.class)
    public ResponseEntity<ResultadoVotacao> contabilizarVotos(@PathVariable Long pautaId) {
        ResultadoVotacao resultado = votoService.contabilizarVotos(pautaId);
        return ResponseEntity.ok(resultado);
    }
}


