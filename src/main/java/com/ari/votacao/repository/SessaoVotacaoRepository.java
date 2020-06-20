package com.ari.votacao.repository;

import com.ari.votacao.entity.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    List<SessaoVotacao> findBySituacao_Ativa();

    boolean existsBySituacao_AtivaAndId(Long id);

    boolean existsById(Long id);
}
