package com.sicredi.sessionManager.repository;

import com.sicredi.sessionManager.entity.PautaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PautaRepository extends JpaRepository<PautaEntity, Long> {

}

