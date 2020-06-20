package com.ari.votacao.adapter;

import com.ari.votacao.converter.LocalDateTimeConverter;
import com.ari.votacao.dto.SessaoVotacaoDTO;
import com.ari.votacao.entity.SessaoVotacao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class SessaoVotacaoAdapter implements Adapter<SessaoVotacao, SessaoVotacaoDTO> {
	private LocalDateTimeConverter converter;

	@Override
	public SessaoVotacao convertTo(SessaoVotacaoDTO dto) {
		return SessaoVotacao.builder()
				.dataHoraFim(converter.convertToEntityAttribute(dto.getDataHoraFim()))
				.dataHoraInicio(converter.convertToEntityAttribute(dto.getDataHoraInicio()))
				.id(dto.getId())
				.situacao(dto.getSituacao())
				.build();
	}

	@Override
	public SessaoVotacaoDTO convertFrom(SessaoVotacao entity) {
		return SessaoVotacaoDTO.builder()
				.dataHoraFim(converter.convertToDatabaseColumn(entity.getDataHoraFim()))
				.dataHoraInicio(converter.convertToDatabaseColumn(entity.getDataHoraInicio()))
				.id(entity.getId())
				.situacao(entity.getSituacao())
				.build();
	}
}
