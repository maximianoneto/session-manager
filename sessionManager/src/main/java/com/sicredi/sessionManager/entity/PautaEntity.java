package com.sicredi.sessionManager.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Entity
@Data
@Table(name = "pauta_entity")
public class PautaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "titulo")
    private String titulo;
    @ApiModelProperty(hidden = true)
    @Column(name = "sessao_aberta")
    private boolean sessaoAberta;
    @ApiModelProperty(hidden = true)
    @Column(name = "duracao_sessao")
    private Duration duracaoSessao;

    @ApiModelProperty(hidden = true)
    @Column(name = "data_hora_inicio_sessao")
    private LocalDateTime dataHoraInicioSessao;


}
