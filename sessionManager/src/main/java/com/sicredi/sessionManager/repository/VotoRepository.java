package com.sicredi.sessionManager.repository;
import com.sicredi.sessionManager.entity.PautaEntity;
import com.sicredi.sessionManager.entity.VotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VotoRepository extends JpaRepository<VotoEntity, Long> {
    List<VotoEntity> findByPauta(PautaEntity pauta);
    boolean existsByPautaIdAndIdAssociado(Long pautaId, Long idAssociado);
}

