package com.ari.votacao.adapter;

import com.ari.votacao.dto.SessaoVotacaoDTO;
import com.ari.votacao.entity.SessaoVotacao;
import org.springframework.stereotype.Component;

@Component
public class SessaoVotacaoAdapter implements Adapter<SessaoVotacao, SessaoVotacaoDTO> {

	@Override
	public SessaoVotacao convertTo(SessaoVotacaoDTO dto) {
		return SessaoVotacao.builder()
				.dataHoraFim(dto.getDataHoraFim())
				.dataHoraInicio(dto.getDataHoraInicio())
				.id(dto.getId())
				.situacao(dto.getSituacao())
				.build();
	}

	@Override
	public SessaoVotacaoDTO convertFrom(SessaoVotacao entity) {
		return SessaoVotacaoDTO.builder()
				.dataHoraFim(entity.getDataHoraFim())
				.dataHoraInicio(entity.getDataHoraInicio())
				.id(entity.getId())
				.situacao(entity.getSituacao())
				.build();
	}
}
