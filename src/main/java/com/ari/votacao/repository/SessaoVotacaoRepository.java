package com.ari.votacao.repository;

import com.ari.votacao.entity.SessaoVotacao;
import com.ari.votacao.enums.SituacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    List<SessaoVotacao> findBySituacao(SituacaoEnum situacao);

    boolean existsBySituacaoAndId(SituacaoEnum situacao, Long id);

    boolean existsById(Long id);
}
