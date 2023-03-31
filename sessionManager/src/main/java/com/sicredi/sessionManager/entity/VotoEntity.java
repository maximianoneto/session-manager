package com.sicredi.sessionManager.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.persistence.*;


@Entity
@Data
@Table(name = "voto_entity")
public class VotoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(hidden = true)
        private Long id;

        @Column(name = "id_associado")
        private Long idAssociado;

        @Column(name = "voto")
        private boolean voto; // true para SIM, false para NAO

        @ManyToOne
        @JoinColumn(name = "pauta_id")
        @ApiModelProperty(hidden = true)
        private PautaEntity pauta;

        @ApiModelProperty(hidden = true)
        @Column(name = "cpf")
        private Long cpf;
}



