package com.ari.votacao.repository;

import com.ari.votacao.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

	boolean existsById(Long id);
}
