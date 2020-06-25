package com.ari.votacao.adapter;

import com.ari.votacao.dto.VotacaoDTO;
import com.ari.votacao.entity.Votacao;
import org.springframework.stereotype.Component;

@Component
public class VotacaoAdapter implements Adapter<Votacao, VotacaoDTO> {
	private final AssociadoAdapter associadoAdapter;
	private final PautaAdapter pautaAdapter;
	private final SessaoVotacaoAdapter sessaoVotacaoAdapter;

	public VotacaoAdapter(AssociadoAdapter associadoAdapter, PautaAdapter pautaAdapter, SessaoVotacaoAdapter sessaoVotacaoAdapter) {
		this.associadoAdapter = associadoAdapter;
		this.pautaAdapter = pautaAdapter;
		this.sessaoVotacaoAdapter = sessaoVotacaoAdapter;
	}

	@Override
	public Votacao convertTo(VotacaoDTO dto) {
		return Votacao.builder()
				.associado(associadoAdapter.convertTo(dto.getAssociado()))
				.id(dto.getId())
				.pauta(pautaAdapter.convertTo(dto.getPauta()))
				.sessaoVotacao(sessaoVotacaoAdapter.convertTo(dto.getSessaoVotacao()))
				.voto(dto.getVoto())
				.build();
	}

	@Override
	public VotacaoDTO convertFrom(Votacao entity) {
		return VotacaoDTO.builder()
				.associado(associadoAdapter.convertFrom(entity.getAssociado()))
				.id(entity.getId())
				.pauta(pautaAdapter.convertFrom(entity.getPauta()))
				.sessaoVotacao(sessaoVotacaoAdapter.convertFrom(entity.getSessaoVotacao()))
				.voto(entity.getVoto())
				.build();
	}
}
