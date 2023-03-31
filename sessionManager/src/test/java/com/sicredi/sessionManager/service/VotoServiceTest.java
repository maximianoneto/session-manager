package com.sicredi.sessionManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sicredi.sessionManager.dto.ResultadoVotacao;
import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.entity.VotoEntity;
import com.sicredi.sessionManager.repository.PautaRepository;
import com.sicredi.sessionManager.repository.VotoRepository;
import org.junit.Before;
import org.junit.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import utils.CPFGenerator;


@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class VotoServiceTest {

    @MockBean
    private VotoRepository votoRepository;

    @MockBean
    private PautaRepository pautaRepository;

    @Autowired
    private VotoService votoService;

    @MockBean
    private RestTemplate restTemplate;

    @Mock
    private RabbitMQPublisher rabbitMQPublisher;

    private PautaEntity pauta;
    private VotoEntity voto;

    @Before
    public void setUp() {
        pauta = new PautaEntity();
        pauta.setId(1L);
        pauta.setDescricao("Pauta de teste");
        pauta.setTitulo("Pauta de titulo");
        pauta.setDataHoraInicioSessao(LocalDateTime.now());
        pauta.setDuracaoSessao(Duration.ofMinutes(10));

        voto = new VotoEntity();
        voto.setId(1L);
        voto.setIdAssociado(1L);
        voto.setVoto(false);

        voto.setPauta(pauta);
    }

    @Test
    public void testRegistrarVoto() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByPautaIdAndIdAssociado(1L, 2L)).thenReturn(true);
        ResponseEntity<?> response = votoService.registrarVoto(1L, voto);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        verify(votoRepository).existsByPautaIdAndIdAssociado(1L, 1L);
        verify(votoRepository).save(any(VotoEntity.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(votoRepository, times(1)).existsByPautaIdAndIdAssociado(any(Long.class), any(Long.class));
        verify(votoRepository, times(1)).save(any(VotoEntity.class));
    }

    @Test
    public void testRegistrarVotoDuplicado() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByPautaIdAndIdAssociado(1L, 1L)).thenReturn(true);

        ResponseEntity<?> response = votoService.registrarVoto(1L, voto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(votoRepository).existsByPautaIdAndIdAssociado(1L, 1L);
        verify(votoRepository, never()).save(any(VotoEntity.class));
    }


    @Test
    public void testContabilizarVotos() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPauta(pauta)).thenReturn(Arrays.asList(voto, voto, voto));

        ResultadoVotacao resultado = votoService.contabilizarVotos(1L);

        assertEquals(0, resultado.getVotosSim());
        assertEquals(3, resultado.getVotosNao());
    }



}




