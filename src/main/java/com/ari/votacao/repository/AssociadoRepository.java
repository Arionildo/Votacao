package com.ari.votacao.repository;

import com.ari.votacao.entity.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    boolean existsByCpfAndPautaId(String cpf, Long id);
}
