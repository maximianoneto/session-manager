package com.sicredi.sessionManager.service;

import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    private PautaEntity pautaEntity;

    @BeforeEach
    public void setUp() {
        pautaEntity = new PautaEntity();
        pautaEntity.setId(1L);
        pautaEntity.setSessaoAberta(false);
    }
    @Test
    void criarPautaTest() {
        PautaEntity pauta = new PautaEntity();
        pauta.setTitulo("Nome da Pauta");
        pauta.setDescricao("Descricao da Pauta");

        when(pautaRepository.save(pauta)).thenReturn(pauta);

        PautaEntity result = pautaService.criarPauta(pauta);

        assertEquals(pauta, result);
    }

    @Test
    public void testAbrirSessao() {
        // Arrange
        Long pautaId = 1L;
        Duration duracao = Duration.ofMinutes(1);
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaEntity));
        when(pautaRepository.save(any(PautaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PautaEntity result = pautaService.abrirSessao(pautaId, duracao);

        // Assert
        assertEquals(pautaId, result.getId());
        assertEquals(true, result.isSessaoAberta());
        assertEquals(duracao, result.getDuracaoSessao());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(result.getDataHoraInicioSessao().isBefore(now.plusSeconds(1)) && result.getDataHoraInicioSessao().isAfter(now.minusSeconds(1)));
        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, times(1)).save(pautaEntity);
    }
}
