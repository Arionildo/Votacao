package com.ari.votacao.repository;

import com.ari.votacao.entity.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long> {

    Integer countVotacaoByPautaIdAndSessaoVotacaoIdAndVoto(Integer idPauta, Integer idSessaoVotacao, Boolean voto);
}
