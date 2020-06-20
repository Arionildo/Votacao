package com.ari.votacao.adapter;

import com.ari.votacao.dto.VotacaoDTO;
import com.ari.votacao.entity.Votacao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class VotacaoAdapter implements Adapter<Votacao, VotacaoDTO> {
	private AssociadoAdapter associadoAdapter;
	private PautaAdapter pautaAdapter;
	private SessaoVotacaoAdapter sessaoVotacaoAdapter;

	@Override
	public Votacao convertTo(VotacaoDTO dto) {
		return Votacao.builder()
				.associado(associadoAdapter.convertTo(dto.getAssociado()))
				.id(dto.getId())
				.Pauta(pautaAdapter.convertTo(dto.getPauta()))
				.sessaoVotacao(sessaoVotacaoAdapter.convertTo(dto.getSessaoVotacao()))
				.voto(dto.getVoto())
				.build();
	}

	@Override
	public VotacaoDTO convertFrom(Votacao entity) {
		return VotacaoDTO.builder()
				.associado(associadoAdapter.convertFrom(entity.getAssociado()))
				.id(entity.getId())
				.Pauta(pautaAdapter.convertFrom(entity.getPauta()))
				.sessaoVotacao(sessaoVotacaoAdapter.convertFrom(entity.getSessaoVotacao()))
				.voto(entity.getVoto())
				.build();
	}
}
