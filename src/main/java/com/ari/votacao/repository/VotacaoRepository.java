package com.ari.votacao.repository;

import com.ari.votacao.entity.Votacao;
import com.ari.votacao.enums.VotoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long> {

    Integer countVotacaoByPautaIdAndSessaoVotacaoIdAndVoto(Long idPauta, Long idSessaoVotacao, VotoEnum voto);
}
